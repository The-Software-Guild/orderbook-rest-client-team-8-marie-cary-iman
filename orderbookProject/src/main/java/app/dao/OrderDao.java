package app.dao;

import app.dto.Order;

import java.util.List;

public interface OrderDao {
  List<Order> getAllOrders();
  List<Order> getSellOrders();
  List<Order> getBuyOrders();

  Order getOrder(int orderId);

  Order addOrder(Order newOrder);

  void updateOrder(Order order);

  void cancelOrder(int orderId);
}
