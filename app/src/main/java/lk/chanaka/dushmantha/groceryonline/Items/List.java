package lk.chanaka.dushmantha.groceryonline.Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import lk.chanaka.dushmantha.groceryonline.Cart.CartActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.OrderList.OrdersActivity;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class List extends AppCompatActivity {
    RecyclerView recyclerView;
    java.util.List<GroceryItem> groceryItems;
    private static String URL;
    private String host;
    private SessionManager sessionManager;
    private String token;
    lk.chanaka.dushmantha.groceryonline.Items.Adapter adapter;
    Toolbar toolbar;
    View shimmerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        host = ((MyApp) this.getApplication()).getServiceURL();


        sessionManager = new SessionManager(List.this);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        String shopid = sessionManager.getShopId();
        URL = host+"/getAllItem/"+shopid;

        recyclerView = findViewById(R.id.itemList2);
        toolbar = findViewById(R.id.toolbar);
        shimmerItem = findViewById(R.id.shimmerItem);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        groceryItems = new ArrayList<>();
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
                                shimmerItem.setVisibility(View.GONE);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(List.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new Adapter(getApplicationContext(),groceryItems);
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
                        Toast.makeText(List.this, errorMsg, Toast.LENGTH_LONG).show();
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
    private void setAdaptor(JSONArray data){

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setId(detailsObject.getString("id"));
                groceryItem.setName(detailsObject.getString("name"));
                groceryItem.setDescription(detailsObject.getString("description"));
                groceryItem.setPrice(detailsObject.getString("price"));
                groceryItem.setDiscount(detailsObject.getString("discount"));
                groceryItem.setImage_url(detailsObject.getString("image_url"));
                groceryItems.add(groceryItem);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*{
            "id": 1,
            "name": "sugar",
            "description": "white sugar",
            "price": "100.00",
            "quantity": "100750",
            "discount": "13.00",
            "category_id": 1,
            "quantity_type_id": 2,
            "shop_id": 1,
            "image_url": "http://10.0.2.2:8000/storage/common_media/c07d684946ecde21367ecac04837b362.jpg",
            "created_at": "2020-03-05 00:00:00",
            "updated_at": "2020-05-19 18:31:16",
            "quantity_type": {
                "id": 2,
                "name": "loose",
                "unit1": "Kg",
                "unit2": "g"
            }
        }*/

    public void logout(View view) {
        sessionManager.logout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search_view){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void order(View view) {
        Intent intent = new Intent(List.this, OrdersActivity.class);
        startActivity(intent);
    }

    public void cart(View view) {
        Intent intent = new Intent(List.this, CartActivity.class);
        startActivity(intent);
    }
}