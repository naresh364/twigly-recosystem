package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 29/08/16.
 */
public class Order {
    public long order_id;
    public List<OrderDetail> orderDetails = new ArrayList<>();
    public long time;

    public Order(OrderData orderData) {
        process(orderData);
    }

    public void process(OrderData orderData) {
        this.order_id = orderData.order_id;
        this.time = orderData.time;
        OrderDetail orderDetail = new OrderDetail(orderData);
        this.orderDetails.add(orderDetail);
    }
}
