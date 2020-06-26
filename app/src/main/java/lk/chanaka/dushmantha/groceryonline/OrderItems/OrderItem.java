package lk.chanaka.dushmantha.groceryonline.OrderItems;

public class OrderItem {
    private String id;
    private String order_id;
    private String item_id;
    private String name;
    private String description;
    private String image_url;
    private String price;
    private String discount;
    private String quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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
}
/*              "id": 15,
                "order_id": 12,
                "price": "55.00",
                "discount": null,
                "item_id": 2,
                "quantity": "1",
                "created_at": "2020-06-26 09:38:00",
                "updated_at": "2020-06-26 09:38:00",
                "name": "sunlight",
                "description": "sunlight 65g",
                "image_url": "http://10.0.2.2:8000ADGGHGHF456asdfre456789"*/