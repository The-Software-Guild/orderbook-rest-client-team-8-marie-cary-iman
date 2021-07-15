package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;
import org.json.JSONArray;

import java.util.List;


public interface OrderBookServiceLayer {

    List<Order> getAllOrders();
    List<Order> getCurrentOrders();
    List<Order> getOrdersByClientId(int clientId) throws UnexpectedClientStateError;

    Order addOrder(Order order) throws UnexpectedOrderStateError, UnexpectedClientStateError;

    boolean updateOrder(Order order) throws UnexpectedOrderStateError;

    boolean cancelOrder(int order) throws UnexpectedOrderStateError;
}
