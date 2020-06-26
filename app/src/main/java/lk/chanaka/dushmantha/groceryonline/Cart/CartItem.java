package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CartItem {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    ArrayList<Cart> cartArrayList;
    Type type;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CART";

    public CartItem(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        type = new TypeToken<List<Cart>>() {}.getType();

        String cart01 = sharedPreferences.getString("CART01", null);


        if (sharedPreferences.contains("CART01")){
            cartArrayList = new Gson().fromJson(cart01, type);
        }
        else {
            cartArrayList = new ArrayList<>();
        }
    }

    public void SavePreference(){
        try {
            String json = new Gson().toJson(cartArrayList, type);
            editor.putString("CART01",json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
        Toast.makeText(context, "Add to Cart Successfully", Toast.LENGTH_SHORT).show();
    }

    public void addCart(String ItemId, String quantity1, String quantity2){
        Cart cart = new Cart(ItemId, quantity1, quantity2);
        cartArrayList.add(cart);
    }

    public ArrayList<Cart> readPreference(){
        return cartArrayList;
    }
}
