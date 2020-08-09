package lk.chanaka.dushmantha.groceryonline.Items;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.User.LoginActivity;
import lk.chanaka.dushmantha.groceryonline.User.ProfilePicture;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.User.RegisterActivity;
import lk.chanaka.dushmantha.groceryonline.SessionManager;
import lk.chanaka.dushmantha.groceryonline.Shop.ShopActivity;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    NavigationView navigationView;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_user, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setUserDetails();

        if(sessionManager.isLogin()){
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }



        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.logout();
                return false;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_call).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getString(R.string.mobile_number)));
                startActivity(intent);
                return false;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_login).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //sessionManager.checkLogin();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_user).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item){
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("UPDATE", true);
                intent.putExtra("TOKEN", sessionManager.getToken());
                startActivity(intent);
                return false;
            }
        });

        if(!sessionManager.isShop()){
            startActivity(new Intent(MainActivity.this, ShopActivity.class));
            finish();
        }
    }

    private void setUserDetails() {
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.nav_profilePic);
        TextView nav_name = navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        TextView nav_email = this.navigationView.getHeaderView(0).findViewById(R.id.nav_email);

        nav_name.setText(sessionManager.getName());
        nav_email.setText(sessionManager.getEmail());
        String image = "";
        image = sessionManager.getImage();
        //Log.d("image", image);

        if(image!=null){
            if(!image.equals("")){
                if (!image.equals("null")) {
                    Picasso.get().load(image).into(imageView);
                }
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePicture.class);
                intent.putExtra("UPDATE", true);
                startActivity(intent);
            }
        });
    }

    /*private void extractItems() {
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
                                if(data.length()==0){
                                    shimmerItem.setVisibility(View.GONE);
                                    emptycart.setVisibility(View.VISIBLE);
                                    //Toast.makeText(MainActivity.this, "Item list Empty!", Toast.LENGTH_LONG).show();
                                }else{
                                    setAdaptor(data);
                                    shimmerItem.setVisibility(View.GONE);
                                }

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }) *//*{
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
        }*//*;

        queue.add(stringRequest);

    }*/
    /*private void setAdaptor(JSONArray data){

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
    }*/
    /*public void logout(View view) {
        sessionManager.logout();
    }
    public void order(View view) {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }
    public void cart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search_view){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}
