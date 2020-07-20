package lk.chanaka.dushmantha.groceryonline.OrderList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String host, token, URL;
    SessionManager sessionManager;
    ArrayList<OrderItem> orderItems;
    private ImageView emptycart;
    AdapterOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        SetToolbar();
        recyclerView = findViewById(R.id.OrderList);
        emptycart = findViewById(R.id.emptycart);

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
                                if(data.length()==0){
                                    emptycart.setVisibility(View.VISIBLE);
                                    Toast.makeText(OrdersActivity.this, "Order list Empty!", Toast.LENGTH_LONG).show();
                                }else{
                                    setAdaptor(data);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrdersActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new AdapterOrder(OrdersActivity.this,orderItems, host, token);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = "Error";
                if (error instanceof NoConnectionError) {
                    errorMsg = getString(R.string.noConnectionError);
                } else if (error instanceof TimeoutError) {
                    errorMsg = getString(R.string.timeoutError);
                } else if (error instanceof AuthFailureError) {
                    errorMsg = getString(R.string.authFailureError);
                } else if (error instanceof ServerError) {
                    errorMsg = getString(R.string.serverError);
                } else if (error instanceof NetworkError) {
                    errorMsg = getString(R.string.networkError);
                } else if (error instanceof ParseError) {
                    errorMsg = getString(R.string.parseError);
                }
                Toast.makeText(OrdersActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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
                orderItem.setTotal_amount(detailsObject.getString("gross_amount").toString());
                orderItem.setAt(detailsObject.getString("created_at".toString()));
                orderItem.setDelivery_charge(detailsObject.getString("delivery_charge"));
                orderItem.setCoupon_off(detailsObject.getString("coupon_off"));
                orderItem.setNet_total(detailsObject.getString("total_amount"));
                orderItem.setFeedback(detailsObject.getBoolean("feedback"));
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
        this.getSupportActionBar().setTitle("ORDERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
