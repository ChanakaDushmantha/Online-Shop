package lk.chanaka.dushmantha.groceryonline.ItemQuntity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class PostOrder {
    private View view;
    private String URL;
    private String token;
    private Context context;
    private String host;

    public PostOrder( Context context) {
        //this.view = view;
        this.context = context;
    }
    public void postOrderbyId(String ItemId, String quantity, String address){

        SessionManager sessionManager = new SessionManager(context);
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
        URL = host+"/getItemById/";
        System.out.println(URL);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(context, "Register Success!", Toast.LENGTH_SHORT).show();

                                /*Intent intent = new Intent(Register.this, ProfilePicture.class);
                                startActivity(intent);
                                finish();*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Register Error 2 ! "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", ItemId);
                params.put("quantity", quantity);
                params.put("address", address);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
