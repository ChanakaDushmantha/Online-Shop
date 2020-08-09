package lk.chanaka.dushmantha.groceryonline.Items;

import org.json.JSONArray;

public interface ItemsView {
    void hideShimmer();
    void showEmpty();
    void setAdaptor(JSONArray item);
}
