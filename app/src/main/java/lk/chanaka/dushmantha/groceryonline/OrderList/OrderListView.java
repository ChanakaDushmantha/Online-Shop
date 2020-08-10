package lk.chanaka.dushmantha.groceryonline.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OrderListView {
    void showEmpty();
    void setAdaptor(JSONArray item);
}
