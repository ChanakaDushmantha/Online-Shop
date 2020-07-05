package lk.chanaka.dushmantha.groceryonline.ItemDetails;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class ItemPresenter {
    private ItemView view;
    private String URL;
    //private String token;
    private Context context;
    private String host;

    public ItemPresenter(ItemView view, Context context) {
        this.view = view;
        this.context = context;
    }
    public void getItemById(String id){

        SessionManager sessionManager = new SessionManager(context);
        //token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
        URL = host+"/getItemById/"+id;

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
                String errorMsg = "Error";
                if (error instanceof NoConnectionError) {
                    errorMsg = context.getString(R.string.noConnectionError);
                } else if (error instanceof TimeoutError) {
                    errorMsg = context.getString(R.string.timeoutError);
                } else if (error instanceof AuthFailureError) {
                    errorMsg = context.getString(R.string.authFailureError);
                } else if (error instanceof ServerError) {
                    errorMsg = context.getString(R.string.serverError);
                } else if (error instanceof NetworkError) {
                    errorMsg = context.getString(R.string.networkError);
                } else if (error instanceof ParseError) {
                    errorMsg = context.getString(R.string.parseError);
                }
                view.onErrorLoading(errorMsg);
                view.hideLoading();

            }
        }) /*{
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
        }*/;
        queue.add(stringRequest);

    }
}
