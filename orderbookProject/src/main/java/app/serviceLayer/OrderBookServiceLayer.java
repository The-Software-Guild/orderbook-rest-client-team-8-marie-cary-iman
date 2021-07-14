package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;

import java.util.List;


public interface OrderBookServiceLayer {

    List<Order> getAllOrders();
    List<Order> getCurrentOrders();

    Order addOrder(Order order) throws UnexpectedOrderStateError, UnexpectedClientStateError;

    boolean updateOrder(Order order) throws UnexpectedOrderStateError;

    boolean cancelOrder(int order) throws UnexpectedOrderStateError;

    //Order checkTradePairs(Order order);
    //void executeValidOrder(Order sellOrder,Order buyOrder);
    //Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity);
}
