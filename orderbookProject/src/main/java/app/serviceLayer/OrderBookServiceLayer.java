package app.serviceLayer;

import app.dto.Order;
import app.dto.Trade;

import java.util.ArrayList;

public interface OrderBookServiceLayer {
    ArrayList<Order> getSellList();
   ArrayList<Order> getBuyList();
   void setSellList(ArrayList<Order> orders);
   void setBuyList(ArrayList<Order> orders);
    Order checkValidOrder(Order order);
    void executeValidOrder(Order sellOrder,Order buyOrder);
    Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity);
}
