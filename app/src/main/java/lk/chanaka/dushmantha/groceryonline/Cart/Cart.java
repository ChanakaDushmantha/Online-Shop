package lk.chanaka.dushmantha.groceryonline.Cart;

public class Cart {
    private String id;
    private String item_id;
    private String name;
    private String description;
    private String image_url;
    private String price;
    private String discount;
    private String quantity;
    private String total;

    public Cart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
/*          "id": 1,
            "order_id": null,
            "price": null,
            "discount": null,
            "item_id": 2,
            "user_id": 3,
            "shop_id": 1,
            "quantity": "3",
            "created_at": "2020-06-30 10:27:28",
            "updated_at": "2020-06-30 10:27:28",
            "item": {
                "id": 2,
                "name": "sunlight",
                "description": "sunlight 65g",
                "price": "55.00",
                "quantity": "10",
                "discount": null,
                "category_id": 2,
                "quantity_type_id": 1,
                "shop_id": 1,
                "image_url": "http://10.0.2.2:8000ADGGHGHF456asdfre456789",
                "created_at": "2020-03-05 00:00:00",
                "updated_at": "2020-03-05 00:00:00"
            }*/