package lk.chanaka.dushmantha.groceryonline.Items;

import com.google.errorprone.annotations.Var;

public class GroceryItem {
    private String id;
    private String name;
    private String description;
    private String category_id;
    private String shop_id;
    private String price;
    private String quantity;
    private String quantity_type;
    private String discount;
    private String image_url;

    public GroceryItem() {
    }

    public GroceryItem(String id, String name, String description, String category_id, String shop_id, String price, String quantity, String quantity_type, String discount, String image_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
        this.shop_id = shop_id;
        this.price = price;
        this.quantity = quantity;
        this.quantity_type = quantity_type;
        this.discount = discount;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity_type() {
        return quantity_type;
    }

    public void setQuantity_type(String quantity_type) {
        this.quantity_type = quantity_type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

/*
        "id": 1,
        "name": "sugar",
        "description": "white sugar",
        "category_id": 1,
        "shop_id": 1,
        "price": "100.00",
        "quantity": "10kg",
        "quantity_type": "loose",
        "discount": null,
        "image_url": "http://127.0.0.1:8000ADGGHGHF456asdfre",
        "created_at": "2020-03-05 00:00:00",
        "updated_at": "2020-03-05 00:00:00",
        "item_category": {
        "id": 1,
        "name": "Sugar"
        },
        "shop": [
        {
        "id": 1,
        "name": "Ns stores"
        }
        ]*/
