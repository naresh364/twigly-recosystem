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
        OrderDetail orderDetail = findOrderDetail(orderData.menu_item_id);
        if (orderDetail == null) {
            orderDetail = new OrderDetail(orderData);
            this.orderDetails.add(orderDetail);
        } else {
            orderDetail.quantity += orderData.quantity >3 ? 3 : orderData.quantity;
        }
    }

    public OrderDetail findOrderDetail(long menu_item_id) {
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.menu_item_id == menu_item_id) {
                return orderDetail;
            }
        }
        return null;
    }
}
