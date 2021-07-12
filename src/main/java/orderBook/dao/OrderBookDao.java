package orderBook.dao;

import orderBook.dto.Order;

import java.util.List;

public interface OrderBookDao {
    List<Order> getAllOrders();

    Order getOrder(int orderId);

    Order addOrder(Order newOrder);

    void updateOrder(Order order);

    void cancelOrder(int orderId);
}
