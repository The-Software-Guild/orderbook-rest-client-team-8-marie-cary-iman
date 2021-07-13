package app.serviceLayer;


import app.dao.OrderBookDao;
import app.dto.Order;
import app.dto.Trade;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderBookServiceLayerImpl implements OrderBookServiceLayer{

    ArrayList<Order> sellOrders = new ArrayList<>();
    ArrayList<Order> buyOrders = new ArrayList<>();
    @Override
    public void setSellList(ArrayList<Order> orders) {
        System.out.println("Set All Selling Active Order");


        sellOrders = orders;

    }

    @Override
    public void setBuyList(ArrayList<Order> orders) {
        System.out.println("Set All Buying Active Order");


        buyOrders = orders;

    }
    @Override
    public ArrayList<Order> getSellList() {
        System.out.println("Get All Selling Active Order");

        return sellOrders;
    }

    @Override
    public ArrayList<Order> getBuyList() {
        System.out.println("Get All Buying Active Order");
        return buyOrders;
    }



    @Override
    public Order checkValidOrder(Order order) {

        //need more logic to sell to the most profitable couple
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
        // need more verification and foolproof if statement
        int buyQuantity = buyOrder.getCumulativeQuantity();
        int sellQuantity = sellOrder.getCumulativeQuantity();
        int soldQuantity = Math.abs(buyQuantity - sellQuantity);
        if(buyQuantity > sellQuantity){
            createTrade(buyOrder, sellOrder, soldQuantity);
            buyOrder.setOrderStatus("partial");
            buyOrder.setCumulativeQuantity(sellQuantity - buyQuantity);
            sellOrder.setCumulativeQuantity(0);
            sellOrder.setOrderStatus("completed");
        }else if(buyQuantity < sellQuantity){
            createTrade(buyOrder, sellOrder, soldQuantity);
            buyOrder.setOrderStatus("completed");
            buyOrder.setCumulativeQuantity(0);
            sellOrder.setCumulativeQuantity(buyQuantity - sellQuantity);
            sellOrder.setOrderStatus("partial");
        }else{
            createTrade(buyOrder, sellOrder, soldQuantity);
            buyOrder.setOrderStatus("completed");
            buyOrder.setCumulativeQuantity(0);
            sellOrder.setOrderStatus("completed");
            sellOrder.setCumulativeQuantity(0);
        }
        //reset cache
        sellOrders = new ArrayList<>();
        buyOrders = new ArrayList<>();
    }

    @Override
    public Trade createTrade(Order buyOrder, Order sellOrder, int soldQuantity) {
        Trade trade = new Trade();
        //auto increment needed
        trade.setTradeId(1);
        trade.setBuyerId(buyOrder.getClientId());
        //Date type
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        trade.setExecutionTime(timestamp);
        trade.setBuyerPrice(buyOrder.getPrice());
        trade.setSellerId(sellOrder.getClientId());
        trade.setSellerPrice(sellOrder.getPrice());
        return trade;
    }
}
