package lk.chanaka.dushmantha.groceryonline.Cart;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CartView {
    void showEmpty();
    void setAdaptor(JSONArray item);
    void parseData (JSONObject data);
}
