package lk.chanaka.dushmantha.groceryonline.Items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
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
                        //Log.d("tag", "onErrorResponse: " + error.getMessage());
                        Toast.makeText(List.this, "Response Error ! "+error.toString(), Toast.LENGTH_LONG).show();
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
                groceryItem.setId(detailsObject.getString("id").toString());
                groceryItem.setName(detailsObject.getString("name").toString());
                groceryItem.setDescription(detailsObject.getString("description".toString()));
                groceryItem.setPrice(detailsObject.getString("price").toString());
                groceryItem.setDiscount(detailsObject.getString("discount".toString()));
                groceryItem.setImage_url(detailsObject.getString("image_url"));
                groceryItems.add(groceryItem);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

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
}