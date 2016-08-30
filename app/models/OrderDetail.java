package models;

/**
 * Created by naresh on 29/08/16.
 */
public class OrderDetail {
    public long menu_item_id;
    public int quantity;
    public float price;
    public float discount;
    public int rating;

    public OrderDetail(OrderData orderData) {
        processOrderData(orderData);
    }

    public void processOrderData(OrderData orderData) {
        this.menu_item_id = orderData.menu_item_id;
        this.quantity = orderData.quantity;
        this.price = orderData.price;
        this.discount = orderData.discount;
        this.rating = orderData.rating;
    }
}
