package lk.chanaka.dushmantha.groceryonline.Cart;

public class Cart {
    String ItemId;
    String quantity1;
    String quantity2;

    public Cart() {
    }

    public Cart(String itemId, String quantity1, String quantity2) {
        ItemId = itemId;
        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(String quantity1) {
        this.quantity1 = quantity1;
    }

    public String getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(String quantity2) {
        this.quantity2 = quantity2;
    }
}
