package orderBook.service;


import orderBook.dto.Order;
import orderBook.dto.Trade;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderBookServiceLayerImpl implements OrderBookServiceLayer{


    @Override
    public ArrayList<Order> getSellList() {
        System.out.println("Get All Selling Active Order");
        ArrayList<Order> sellOrders = new ArrayList<>();
        return sellOrders;
    }

    @Override
    public ArrayList<Order> getBuyList() {
        System.out.println("Get All Buying Active Order");
        ArrayList<Order> buyOrders = new ArrayList<>();
        return buyOrders;
    }

    @Override
    public Order checkValidOrder(Order order) {
        System.out.println("Compare New order to order Table");
        if (order.getOrderType().equals("ask")) {
            ArrayList<Order> buyOrders = getBuyList();
            for (int i = 0; i < buyOrders.size(); i++) {
                if(buyOrders.get(i).getPrice().compareTo(order.getPrice()) == -1 ){
                    System.out.println("Select this order to sell");
                    break;
                }
            }
        } else {
            ArrayList<Order> sellOrders = getSellList();
            for (int i = 0; i < sellOrders.size(); i++) {
                if(sellOrders.get(i).getPrice().compareTo(order.getPrice()) == 1){
                    System.out.println("Select this order to sell");
                    break;
                }
            }
        }

        return order;
    }

    @Override
    public void executeValidOrder(Order sellOrder,Order buyOrder) {
        int buyQuantity = buyOrder.getCumulativeQuantity();
        int sellQuantity = sellOrder.getCumulativeQuantity();
        int soldQuantity = Math.abs(buyQuantity - sellQuantity);
        if(buyQuantity > sellQuantity){
            buyOrder.setOrderStatus("partial");
            buyOrder.setCumulativeQuantity(buyQuantity - sellQuantity);
            sellOrder.setCumulativeQuantity(0);
            sellOrder.setOrderStatus("completed");
        }else if(buyQuantity < sellQuantity){
            buyOrder.setOrderStatus("completed");
            buyOrder.setCumulativeQuantity(0);
            sellOrder.setCumulativeQuantity(sellQuantity - buyQuantity);
            sellOrder.setOrderStatus("partial");
        }else{
            buyOrder.setOrderStatus("completed");
            buyOrder.setCumulativeQuantity(0);
            sellOrder.setOrderStatus("completed");
            sellOrder.setCumulativeQuantity(0);
        }
        createTrade(buyOrder,sellOrder , soldQuantity);

    }

    @Override
    public Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity) {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setBuyerId(buyOrder.getClientId());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        trade.setExecutionTime(timestamp);
        trade.setBuyerPrice(buyOrder.getPrice());
        trade.setSellerId(sellOrder.getClientId());
        trade.setSellerPrice(sellOrder.getPrice());
        return trade;
    }
}
