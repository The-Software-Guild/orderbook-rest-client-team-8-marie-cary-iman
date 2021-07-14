package app.serviceLayer;

import app.dao.ClientDao;
import app.dao.OrderBookDao;
import app.dao.TradeDao;
import app.dto.Client;
import app.dto.Order;
import app.dto.Trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBookServiceLayerImpl implements OrderBookServiceLayer{
    private JdbcTemplate jdbc;

    @Autowired
    OrderBookDao orderDao;

    @Autowired
    ClientDao clientDao;

    @Autowired
    TradeDao tradeDao;

    OrderBookServiceLayerImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    private boolean existingClient(int clientId){
        List<Client> allClients = clientDao.getAllClients();
        List<Client> existingClient = allClients.stream()
                .filter(c -> c.getClientId() == clientId)
                .collect(Collectors.toList());

        return (!existingClient.isEmpty());
    };

    private boolean existingOrder(Order order){
        boolean match;
        List<Order> allOrders = getAllOrders();
        List<Order> existingOrder = allOrders.stream()
                .filter(o -> o.getOrderId() == order.getOrderId())
                .collect(Collectors.toList());

        return !existingOrder.isEmpty();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public List<Order> getCurrentOrders() {
        return orderDao.getCurrentOrders();
    }

    @Override
    public Order addOrder(Order order) throws UnexpectedOrderStateError, UnexpectedClientStateError {
        boolean orderExists = existingOrder(order);
        boolean clientExists = existingClient(order.getClientId());

        if (!orderExists && clientExists){
            Order newOrder = orderDao.addOrder(order);
            Order pairedOrder = checkValidOrder(newOrder);

            if (pairedOrder != newOrder){
                executeValidOrder();
            }

        } else if (orderExists)
            throw new UnexpectedOrderStateError("This order already exists and thus cannot be created again");
        else {
            throw new UnexpectedClientStateError("This client does not exist, aborted order creation");
        }

        return null;
    }

    @Override
    public boolean updateOrder(Order order) throws UnexpectedOrderStateError {
        if (existingOrder(order))
            return orderDao.updateOrder(order);
        else
            throw new UnexpectedOrderStateError("Order does not exist with that orderId, cannot update.");
    }

    @Override
    public Order checkValidOrder(Order order) {
        System.out.println("Compare New order to order Table");
        if (order.getOrderType().equals("sell")) {
            List<Order> orders = orderDao.getBuyOrders(order.getStockSymbol());
            for (Order order1 : orders) {
                if(order1.getPrice().compareTo(order.getPrice()) == -1 ){
                    order = order1;
                    break;
                }
            }
        } else {
            List<Order> orders = orderDao.getSellOrders(order.getStockSymbol());
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
        trade.setBuyerId(buyOrder.getClientId());
        //Date type
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        trade.setExecutionTime(timestamp);
        trade.setBuyerPrice(buyOrder.getPrice());
        trade.setSellerId(sellOrder.getClientId());
        trade.setSellerPrice(sellOrder.getPrice());
        return tradeDao.addTrade(trade);
    }
}
