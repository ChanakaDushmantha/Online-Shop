package lk.chanaka.dushmantha.groceryonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.Items.List;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;

public class Shops extends AppCompatActivity {

    private ProgressBar loading;
    private CardView btn_next;
    private ListView shoplist;
    private static String URL = "";
    private String host;
    private String token;
    private int shopId = -1;
    SessionManager sessionManager;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        sessionManager = new SessionManager(this);
        token = sessionManager.getToken();

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/getAllShop";

        loading = findViewById(R.id.loading);
        btn_next = findViewById(R.id.btn_next);
        shoplist = findViewById(R.id.shoplist);

        loading.setVisibility(View.VISIBLE);

        /*if(!(new NetworkConnection( this).isNetworkConnected())){
            Toast.makeText(this, "Internet Connection Error", Toast.LENGTH_LONG).show();
        }
        else{*/
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray data = jsonObject.getJSONArray("data");

                                if(success.equals("true")){
                                    //Toast.makeText(Shops.this, "Shop Success!", Toast.LENGTH_SHORT).show();
                                    createList(data);
                                    loading.setVisibility(View.GONE);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Shops.this, "Response type error "+e.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Shops.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer "+token);
                    //System.out.println(token);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        //}

        shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shopId = position+1;
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopId == -1) {
                    Toast.makeText(Shops.this, "Select which shop you favor to buy", Toast.LENGTH_SHORT).show();
                }
                else{
                    //System.out.println(shopId);
                    sessionManager.addShop(shopId);
                    Intent intent = new Intent(Shops.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void createList(JSONArray array) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
           JSONObject jsonObject = array.getJSONObject(i);
           String name = jsonObject.getString("name");
           arrayList.add(name);
        }
        ViewList(arrayList);
    }

    private void ViewList(ArrayList<String> array){
        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,array);
        shoplist.setAdapter(listAdapter);
        shoplist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
