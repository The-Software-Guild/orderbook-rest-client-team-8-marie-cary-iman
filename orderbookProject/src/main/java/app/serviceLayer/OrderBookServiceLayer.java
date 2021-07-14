package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;

import java.util.List;


public interface OrderBookServiceLayer {

    List<Order> getAllOrders();
    List<Order> getCurrentOrders();

    Order checkValidOrder(Order order);
    void executeValidOrder(Order sellOrder,Order buyOrder);
    Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity);
}
