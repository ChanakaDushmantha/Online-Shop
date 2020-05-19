package lk.chanaka.dushmantha.groceryonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.common.collect.Lists;

import java.util.HashMap;

import lk.chanaka.dushmantha.groceryonline.Items.List;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ADDRESS = "ADDRESS";
    public static final String TOKEN = "TOKEN";
    public static final String SHOPID = "SHOPID";

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
        editor.putString(SHOPID, "1");
        editor.apply();

    }

    public void addShop(int id){
        String shopId = String.valueOf(id);
        editor.putString(SHOPID, shopId);
        editor.apply();
    }

    public String getShopId(){
        String shopid = sharedPreferences.getString(SHOPID, null);
        return shopid;
    }

    public String getAddress(){
        String ads = sharedPreferences.getString(ADDRESS, null);
        return ads;
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

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        Activity activity = (Activity) context;
        activity.finish();

    }

}
