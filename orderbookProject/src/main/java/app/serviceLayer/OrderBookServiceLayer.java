package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;
import org.json.JSONArray;

import java.util.List;


public interface OrderBookServiceLayer {

    List<Order> getAllOrders();
    List<Order> getCurrentOrders();
    List<Order> getOrdersByClientId(int clientId);

    List<Trade> getTradesByOrderId(int orderId);

    Order addOrder(Order order) throws UnexpectedOrderStateError, UnexpectedClientStateError;

    boolean updateOrder(Order order) throws UnexpectedOrderStateError;

    boolean cancelOrder(int order) throws UnexpectedOrderStateError;

    Order checkValidOrder(Order order);

    void createTrade(Order createdOrder, Order existingOrder);
}
