package lk.chanaka.dushmantha.groceryonline.ItemDetails;

import org.json.JSONObject;

public interface ItemView {
    public void showLoading();
    public void hideLoading();
    public void setItem(JSONObject item);
    public void onErrorLoading(String message);

}
