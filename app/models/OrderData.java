package models;

/**
 * Created by naresh on 28/08/16.
 */
public class OrderData {
    public long user_id;
    public long order_id;
    public long menu_item_id;
    public int quantity;
    public float price;
    public float discount;
    public int rating;
    public long time;

    public OrderData(){

    }

    public OrderData(long user_id, long order_id, long menu_item_id, int quantity,
                float price, float discount, int rating, long time) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.menu_item_id = menu_item_id;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.rating = rating;
        this.time = time;
    }
}
