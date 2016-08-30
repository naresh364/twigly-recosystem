package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naresh on 29/08/16.
 */
public class User {
    public long user_id;
    public List<Order> orders = new ArrayList<>();
    public Map<Long, Integer> itemCountMap = new HashMap<>();
    public Map<MenuItemBundle, UserParams> itemBundleCountMap = new HashMap<>();

    public User(OrderData orderData) {
        process(orderData);
    }

    public void process(OrderData orderData) {
        this.user_id = orderData.user_id;
        Order order = findOrCreateOrder(orderData.order_id);
        if (order == null) {
            order = new Order(orderData);
            this.orders.add(order);
        } else {
            order.process(orderData);
        }
        int quantity = orderData.quantity > 3?3:orderData.quantity;
        Integer current = itemCountMap.get(orderData.menu_item_id);
        if (current == null) itemCountMap.put(orderData.menu_item_id, quantity);
        else itemCountMap.put(orderData.menu_item_id, current+quantity);

        addtoBundleMap(orderData);
    }

    private void addtoBundleMap(OrderData orderData) {
        MenuItemBundle bundle = MenuItemBundleReverseMap.getInstance().getMenuItemBundleMap().get(orderData.menu_item_id);
        if (bundle == null) return;

        int quantity = orderData.quantity > 3?3:orderData.quantity;
        UserParams current = itemBundleCountMap.get(bundle);
        if (current == null){
            UserParams userParams = new UserParams(quantity, orderData.rating, orderData.time);
            itemBundleCountMap.put(bundle, userParams);
        }
        else {
            current.update(quantity, orderData.rating, orderData.time);
        }

    }

    public Order findOrCreateOrder(long order_id) {
        Order found = null;
        for (Order order: orders) {
            if (order.order_id == order_id) {
                found = order;
                break;
            }
        }
        return found;
    }

    public int getMaxDishCount() {
        int max = 1;
        for (Map.Entry<MenuItemBundle, UserParams> menuItemBundleEntry : itemBundleCountMap.entrySet()) {
            if (max < menuItemBundleEntry.getValue().count) max = menuItemBundleEntry.getValue().count;
        }
        return max;
    }
}
