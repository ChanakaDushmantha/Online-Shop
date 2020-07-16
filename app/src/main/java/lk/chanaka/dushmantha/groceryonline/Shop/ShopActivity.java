package lk.chanaka.dushmantha.groceryonline.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;
import lk.chanaka.dushmantha.groceryonline.Shop.ImageSlider.SliderAdapter;
import lk.chanaka.dushmantha.groceryonline.Shop.ImageSlider.SliderItem;

public class ShopActivity extends AppCompatActivity {

    private static String URL = "";
    private String host;
    ArrayList<Shop> shophouses;
    SessionManager sessionManager;
    SliderView sliderView;
    private SliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        sessionManager = new SessionManager(this);

        host = ((MyApp) this.getApplication()).getServiceURL();


        shophouses = new ArrayList<>();

        extractShop();
        imageSlider();
    }

    private void extractShop(){
        URL = host+"/getAllShop";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(success.equals("true")){
                                //Toast.makeText(ShopActivity.this, "Shop Success!", Toast.LENGTH_SHORT).show();
                                //createList(data);
                                setShop(data);
                                //loading.setVisibility(View.GONE);
                                findViewById(R.id.shimmerShop).setVisibility(View.GONE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShopActivity.this, "Response type error "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                        Toast.makeText(ShopActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                })
            /*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //params.put("Authorization", "Bearer "+token);
                    //System.out.println(token);
                    return params;
                }
            }*/;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void extractBanners(){
        URL = host+"/getBannerImages";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(success.equals("true")){
                                setBanners(data);
                                findViewById(R.id.shimmerBanner).setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShopActivity.this, "Response type error "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                        Toast.makeText(ShopActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                })
            /*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //params.put("Authorization", "Bearer "+token);
                    //System.out.println(token);
                    return params;
                }
            }*/;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void setBanners(JSONArray data) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                SliderItem sliderItem = new SliderItem();
                sliderItem.setImageUrl(detailsObject.getString("image_url"));
                sliderItem.setDescription(detailsObject.getString("description"));
                sliderItemList.add(sliderItem);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        adapter.renewItems(sliderItemList);
    }

    private void imageSlider(){
        sliderView = findViewById(R.id.imageSlider);


        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        extractBanners();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });
    }

    private void setShop(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                Shop shophouse = new Shop();
                shophouse.setShopId(detailsObject.getString("id"));
                shophouse.setName(detailsObject.getString("name"));
                shophouse.setImage_url(detailsObject.getString("image_url"));
                shophouse.setRating(detailsObject.getString("feedback"));
                shophouses.add(shophouse);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        setAdapet(shophouses);
    }
    
    public void setAdapet(List<Shop> shops) {
        ViewPager shopViewPager = findViewById(R.id.viewPagerHeader);
        ShopAdapter headerAdapter = new ShopAdapter(shops, this);
        shopViewPager.setAdapter(headerAdapter);
        shopViewPager.setPadding(20, 0, 150, 0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((v, position) -> {
            //Toast.makeText(this, shops.get(position).getName(), Toast.LENGTH_SHORT).show();
            sessionManager.addShop(Integer.parseInt(shops.get(position).getShopId()));
            Intent intent = new Intent(ShopActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
