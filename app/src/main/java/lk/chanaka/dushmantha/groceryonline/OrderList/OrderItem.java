package lk.chanaka.dushmantha.groceryonline.OrderList;

public class OrderItem {
    private String id;
    private String status;
    private String delivery_address;
    private String total_amount;
    private String at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }
}
/*"id": 5,
        "shop_id": 1,
        "user_id": 4,
        "status": "pending",
        "delivery_address": "dkfkjkfkdfkdfk hhghghcfcff",
        "total_amount": "165.00",
        "created_at": "2020-05-18 10:32:22",
        "updated_at": "2020-05-18 10:32:22",
        "deleted_at": null*/
