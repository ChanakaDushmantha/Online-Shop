package lk.chanaka.dushmantha.groceryonline.User;

public class Shop {
    private String name;
    private String image_url;
    private String rating;
    private String shopId;

    public Shop() {
    }

    public Shop(String name, String image_url, String rating) {
        this.name = name;
        this.image_url = image_url;
        this.rating = rating;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
