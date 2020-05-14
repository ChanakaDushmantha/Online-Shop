package lk.chanaka.dushmantha.groceryonline.ItemDetails;

import android.content.Context;
import android.content.Intent;
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

public class ItemPresenter {
    private ItemView view;
    private String URL;
    private String token;
    private Context context;
    private String host;

    public ItemPresenter(ItemView view, Context context) {
        this.view = view;
        this.context = context;
    }
    public void getItemById(String id){

        SessionManager sessionManager = new SessionManager(context);
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
        URL = host+"/getItemById/"+id;
        System.out.println(URL);

        view.showLoading();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        view.hideLoading();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONObject data = jsonObject.getJSONObject("data");

                            if(success.equals("true")){
                                view.setItem(data);
                            }else{
                                view.onErrorLoading(message);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            view.onErrorLoading(e.getLocalizedMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                view.hideLoading();
                view.onErrorLoading(error.getLocalizedMessage());
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
}