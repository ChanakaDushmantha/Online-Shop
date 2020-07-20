package lk.chanaka.dushmantha.groceryonline.OrderItems;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.OrderList.OrdersActivity;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class OrderItemsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String host, token, URL;
    SessionManager sessionManager;
    ArrayList<OrderItem> orderItems;
    private ImageView emptycart;
    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("ID");

        SetToolbar();
        recyclerView = findViewById(R.id.OrderList);
        emptycart = findViewById(R.id.emptycart);

        host = ((MyApp) this.getApplication()).getServiceURL();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        //String shopid = sessionManager.getShopId();

        URL = host+"/getOrderById/"+orderId;
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray cart = data.getJSONArray("cart");

                            if(success.equals("true")){

                                setAdaptor(cart);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderItemsActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        AdapterOrderItem adapter = new AdapterOrderItem(getApplicationContext(), orderItems, host, token);
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
                Toast.makeText(OrderItemsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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
                orderItem.setId(detailsObject.getString("id"));
                //orderItem.setOrder_id(detailsObject.getString("order_id"));
                orderItem.setItem_id(detailsObject.getString("item_id"));
                orderItem.setName(detailsObject.getString("name"));
                orderItem.setDescription(detailsObject.getString("description"));
                orderItem.setImage_url(detailsObject.getString("image_url"));
                orderItem.setPrice(detailsObject.getString("items_total"));
                orderItem.setDiscount(detailsObject.getString("discount"));
                orderItem.setQuantity(detailsObject.getString("quantity"));
                orderItems.add(orderItem);
                /*"data": {
                        "cart": [
                            {
                                "id": 3,
                                "price": "55.00",
                                "discount": null,
                                "item_id": 2,
                                "quantity": "1",
                                "items_total": 55,
                                "name": "sunlight",
                                "description": "sunlight 65g",
                                "image_url": null
                            }
                        ]
    }*/

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("ORDER ID "+orderId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(this, OrdersActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
