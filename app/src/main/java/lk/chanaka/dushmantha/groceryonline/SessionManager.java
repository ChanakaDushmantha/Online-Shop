package lk.chanaka.dushmantha.groceryonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.Shop.ShopActivity;
import lk.chanaka.dushmantha.groceryonline.User.LoginActivity;


public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String ADDRESS = "ADDRESS";
    private static final String TOKEN = "TOKEN";
    private static final String SHOPID = "SHOPID";
    private static final String IMAGE = "IMAGE";
    private static final String REG_TYPE = "REG_TYPE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String address, String token){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, address);
        editor.putString(TOKEN, token);
        //editor.putString(SHOPID, "1");
        editor.apply();

    }

    public void createSession(String name, String email, String address, String token, String image_url){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, address);
        editor.putString(TOKEN, token);
        editor.putString(IMAGE, image_url);
        //editor.putString(SHOPID, "1");
        editor.apply();

    }

    public void createSession(String name, String email, String address, String token, String image_url, String reg_type){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, address);
        editor.putString(TOKEN, token);
        editor.putString(IMAGE, image_url);
        editor.putString(REG_TYPE, reg_type);
        editor.apply();

    }
    public void addShop(int id){
        String shopId = String.valueOf(id);
        editor.putString(SHOPID, shopId);
        editor.apply();
    }

    public void addImage(String image){
        editor.putString(IMAGE, image);
        editor.apply();
    }

    public String getRegType(){
        return sharedPreferences.getString(REG_TYPE, null);
    }

    public String getImage(){
        String image = sharedPreferences.getString(IMAGE, null);
        return image;
    }

    public String getShopId(){
        String shopid = sharedPreferences.getString(SHOPID, null);
        return shopid;
    }

    public String getAddress(){
        String ads = sharedPreferences.getString(ADDRESS, null);
        return ads;
    }

    public String getName(){
        String ads = sharedPreferences.getString(NAME, null);
        return ads;
    }

    public String getEmail(){
        String ads = sharedPreferences.getString(EMAIL, null);
        return ads;
    }
    public boolean isShop(){
        return getShopId() != null;
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){

        if (!this.isLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            Activity activity = (Activity) context;
            activity.finish();
        }
    }

    public String getToken(){
        String token = sharedPreferences.getString(TOKEN, null);
        return token;
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        user.put(SHOPID, sharedPreferences.getString(SHOPID, null));

        return user;
    }

    public void logout(){
        if(getRegType().equals("google")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
            mGoogleSignInClient.signOut();
            Log.d("OUT", "google signOut");
        }
        if(getRegType().equals("facebook")){
            LoginManager.getInstance().logOut();
            Log.d("OUT", "facebook signOut");
        }

        String host = ((MyApp) context.getApplicationContext()).getServiceURL();
        String URL = host+"/logout";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("true")){
                                editor.clear();
                                editor.commit();
                                Intent i = new Intent(context, LoginActivity.class);
                                context.startActivity(i);
                                Activity activity = (Activity) context;
                                activity.finish();
                                Log.d("OUT", "log out");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Logout error "+e.toString(), Toast.LENGTH_LONG).show();
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer "+getToken());
                    //System.out.println(token);
                    return params;
                }
            };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
