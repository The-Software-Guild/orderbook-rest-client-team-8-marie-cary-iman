package app.serviceLayer;



import app.dao.OrderBookDaoDB;
import app.dto.Order;
import app.dto.Trade;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

import java.util.List;

public class OrderBookServiceLayerImpl implements OrderBookServiceLayer{
    private JdbcTemplate jdbc = new JdbcTemplate();
    OrderBookDaoDB daoDB = new OrderBookDaoDB(jdbc);



    @Override
    public Order checkValidOrder(Order order) {
        System.out.println("Compare New order to order Table");
        if (order.getOrderType().equals("Sell")) {
            List<Order> orders = daoDB.getBuyOrders();
            for (Order order1 : orders) {

                if(order1.getPrice().compareTo(order.getPrice()) == -1 ){
                    order = order1;
                    break;
                }
            }
        } else {
            List<Order> orders = daoDB.getSellOrders();
            for (Order order1 : orders) {
                if(order1.getPrice().compareTo(order.getPrice()) == 1){
                    order = order1;
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
