package lk.chanaka.dushmantha.groceryonline.Cart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import lk.chanaka.dushmantha.groceryonline.ItemQuantity.ConfirmCharges;
import lk.chanaka.dushmantha.groceryonline.ItemQuantity.QuantityActivity;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItem;
import lk.chanaka.dushmantha.groceryonline.OrderList.OrdersActivity;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;


public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String host, token, URL;
    SessionManager sessionManager;
    ArrayList<Cart> cartItems;
    private ImageView emptycart;
    private JSONObject data;
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


        URL = host+"/getAllCartItems/"+shopid;
        cartItems = new ArrayList<>();


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
                            data = jsonObject.getJSONObject("data");
                            JSONArray carts = data.getJSONArray("carts");

                            if(success.equals("true")){
                                if(carts.length()==0){
                                    emptycart.setVisibility(View.VISIBLE);
                                    Toast.makeText(CartActivity.this, "Cart list Empty!", Toast.LENGTH_LONG).show();
                                }else{
                                    setAdaptor(carts);
                                    findViewById(R.id.lyCart).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        AdapterCart adapter = new AdapterCart(getApplicationContext(),cartItems, host, token);
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
                Toast.makeText(CartActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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

                Cart cart = new Cart();
                cart.setId(detailsObject.getString("id").toString());

                cart.setQuantity(detailsObject.getString("quantity").toString());

                JSONObject item = detailsObject.getJSONObject("item");
                cart.setItem_id(item.getString("id").toString());
                cart.setName(item.getString("name".toString()));
                cart.setDescription(item.getString("description").toString());
                cart.setImage_url(item.getString("image_url".toString()));
                cart.setPrice(item.getString("price").toString());
                cart.setDiscount(item.getString("discount".toString()));
                cart.setTotal(item.getString("total").toString());
                cartItems.add(cart);
                /*{
        "delivery_charge": "50.00",
        "cart_total": 165,
        "net_total": 215,
        "carts": [
            {
                "id": 6,
                "quantity": "3",
                "item": {
                    "id": 3,
                    "name": "lux",
                    "description": "lux 65g",
                    "price": "55.00",
                    "quantity": "10",
                    "discount": null,
                    "image_url": "http://10.0.2.2:8000ADGGHGHF456asdfre456789",
                    "total": 165,
                    "quantity_type": {
                        "id": 1,
                        "name": "piece",
                        "unit1": "",
                        "unit2": ""
                    }
                }
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
        this.getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ok(View view) {
        Intent i = new Intent(this, QuantityActivity.class);
        i.putExtra("CART_ORDER", true);
        startActivity(i);
    }

    public void details(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {

            ft.remove(prev);
        }
        ft.addToBackStack(null);
        BottomSheetDialogFragment dialogFragment = new ConfirmCharges();

        Bundle args = new Bundle();
        try {
            args.putString("Total", data.getString("cart_total"));
            args.putString("Delivery_Charge", data.getString("delivery_charge"));
            args.putString("Net_Total", data.getString("net_total"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        args.putString("Coupon_OFF","0.00" );
        args.putString("Status", "CART");

        dialogFragment.setArguments(args);
        dialogFragment.show(ft, "dialog");
    }
}