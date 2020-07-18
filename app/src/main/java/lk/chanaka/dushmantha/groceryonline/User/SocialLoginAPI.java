package lk.chanaka.dushmantha.groceryonline.User;

import android.app.Activity;
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

import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class SocialLoginAPI {
    private Context context;
    private String host;
    private SessionManager sessionManager;
    private Activity activity;

    public SocialLoginAPI(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(context);
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
    }

    public void socialLogin(String provider, String provider_user_id, String provider_token){
        String URL = host+"/socialLogin";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            JSONObject data =  jsonObject.getJSONObject("data");
                            if(success.equals("true")){
                                setData(data);
                                //Toast.makeText(context, "Thank you for feedback!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("provider", String.valueOf(provider));
                params.put("provider_user_id", provider_user_id);
                params.put("provider_token", provider_token);
                /*System.out.println(String.valueOf(feedback_rate));
                System.out.println(feedback_comment);*/
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setData(JSONObject data) {
        String name = "";
        String address = "";
        String email = "";
        String reg_type = "";
        String image_url = "";
        String token = "";

        try {
            name = data.getString("name");
            address = data.getString("address");
            email = data.getString("email");
            reg_type = data.getString("reg_type");
            image_url = data.getString("image_url");
            token = data.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sessionManager.createSession(name, email, address, token, image_url, reg_type);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        activity.finish();
    }
}
