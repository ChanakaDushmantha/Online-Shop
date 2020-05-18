package lk.chanaka.dushmantha.groceryonline.OrderList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String host, token, URL;
    SessionManager sessionManager;
    ArrayList<OrderItem> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        SetToolbar();
        recyclerView = findViewById(R.id.OrderList);

        host = ((MyApp) this.getApplication()).getServiceURL();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        String shopid = sessionManager.getShopId();
        URL = host+"/getAllOrders/"+shopid;
        orderItems = new ArrayList<>();
        extractItems();

    }
    private void extractItems() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(success.equals("true")){
                                //Toast.makeText(List.this, "list Success!", Toast.LENGTH_SHORT).show();
                                setAdaptor(data);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrdersActivity.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        AdapterOrder adapter = new AdapterOrder(getApplicationContext(),orderItems);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(OrdersActivity.this, "Response Error ! "+error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
        };

        queue.add(stringRequest);

    }

    private void setAdaptor(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                OrderItem orderItem = new OrderItem();
                orderItem.setId(detailsObject.getString("id").toString());
                orderItem.setStatus(detailsObject.getString("status").toString());
                orderItem.setDelivery_address(detailsObject.getString("delivery_address".toString()));
                orderItem.setTotal_amount(detailsObject.getString("total_amount").toString());
                orderItem.setAt(detailsObject.getString("created_at".toString()));
                orderItems.add(orderItem);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
