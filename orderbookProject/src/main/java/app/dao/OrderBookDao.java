package app.dao;

import app.dto.Order;

import java.util.List;

public interface OrderBookDao {
  List<Order> getAllOrders();
  List<Order> getAllOrders(String stockSymbol);
  List<Order> getSellOrders(String stockSymbol);
  List<Order> getBuyOrders(String stockSymbol);

  Order getOrder(int orderId);

  Order addOrder(Order newOrder);

  //true if order exists and is updated
  boolean updateOrder(Order order);

  boolean deleteOrderById(int orderId);

  //true if order exists and is canceled
  boolean cancelOrder(int orderId);

  List<Order> getOrdersByClientId(int clientId);

  List<Order> getCurrentOrders();
}
