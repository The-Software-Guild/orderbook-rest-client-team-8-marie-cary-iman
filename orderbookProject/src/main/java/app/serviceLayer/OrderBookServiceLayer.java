package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;

import java.util.ArrayList;

public interface OrderBookServiceLayer {

    Order checkValidOrder(Order order);
    void executeValidOrder(Order sellOrder,Order buyOrder);
    Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity);
}
