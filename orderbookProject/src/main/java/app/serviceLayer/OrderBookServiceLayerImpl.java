package app.serviceLayer;

import app.dao.ClientDao;
import app.dao.OrderBookDao;
import app.dao.TradeDao;
import app.dto.Client;
import app.dto.Order;
import app.dto.Trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBookServiceLayerImpl implements OrderBookServiceLayer{

    @Autowired
    OrderBookDao orderDao;

    @Autowired
    ClientDao clientDao;

    @Autowired
    TradeDao tradeDao;

    private boolean existingClient(int clientId){
        List<Client> allClients = clientDao.getAllClients();
        List<Client> existingClient = allClients.stream()
                .filter(c -> c.getClientId() == clientId)
                .collect(Collectors.toList());

        return (!existingClient.isEmpty());
    };

    private boolean existingOrder(Order order){
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
                createTrade(newOrder, pairedOrder);
            }

            return newOrder;

        } else if (orderExists)
            throw new UnexpectedOrderStateError("This order already exists and thus cannot be created again");
        else {
            throw new UnexpectedClientStateError("This client does not exist, aborted order creation");
        }
    }

    @Override
    public boolean updateOrder(Order order) throws UnexpectedOrderStateError {
        boolean orderUpdated;

        if (existingOrder(order)) {
            orderUpdated = orderDao.updateOrder(order);
            Order toMatch = orderDao.getOrder(order.getOrderId());
            Order matched = checkValidOrder(toMatch);
            if (toMatch != matched) {
                createTrade(toMatch, matched);
            }
            return orderUpdated;
        }
        else
            throw new UnexpectedOrderStateError("Order does not exist with that orderId, cannot update.");
    }

    @Override
    public Order checkValidOrder(Order order) {
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

    /**
     * Takes two orders in as arguments who are matched to have a maximal commission.
     * Adjusts the quantity of stock left in each order post trade.
     *
     * @param firstOrder the first order in the trade
     * @param secondOrder the second order matched with the trade
     */

    @Override
    public int executeTrade(Order firstOrder, Order secondOrder) {
        int firstOrderQty = firstOrder.getCumulativeQuantity();
        int secondOrderQty = secondOrder.getCumulativeQuantity();
        int quantityFilled = 0;
        if(firstOrderQty > secondOrderQty){
            firstOrder.setOrderStatus("partial");
            firstOrder.setCumulativeQuantity(firstOrderQty - secondOrderQty);
            secondOrder.setCumulativeQuantity(0);
            secondOrder.setOrderStatus("completed");
            quantityFilled = secondOrderQty;
        }else if(firstOrderQty < secondOrderQty){
            firstOrder.setOrderStatus("completed");
            firstOrder.setCumulativeQuantity(0);
            secondOrder.setCumulativeQuantity(secondOrderQty - firstOrderQty);
            secondOrder.setOrderStatus("partial");
            quantityFilled = firstOrderQty;
        }else{
            firstOrder.setOrderStatus("completed");
            firstOrder.setCumulativeQuantity(0);
            secondOrder.setOrderStatus("completed");
            secondOrder.setCumulativeQuantity(0);
            quantityFilled = firstOrderQty;
        }
        orderDao.updateOrder(firstOrder);
        orderDao.updateOrder(secondOrder);
        return quantityFilled;
    }

    @Override
    public Trade createTrade(Order createdOrder, Order existingOrder) {

        Trade trade = new Trade();

        if (createdOrder.getOrderType().contains("sell")){
            trade.setSellerId(createdOrder.getClientId());
            trade.setSellerPrice(createdOrder.getPrice());
            trade.setBuyerId(existingOrder.getClientId());
            trade.setBuyerPrice(existingOrder.getPrice());
        } else {
            trade.setSellerId(existingOrder.getClientId());
            trade.setSellerPrice(existingOrder.getPrice());
            trade.setBuyerId(createdOrder.getClientId());
            trade.setBuyerPrice(createdOrder.getPrice());
        }

        // Sets the quantity traded to the return of the trade execution
        trade.setQuantityFilled(executeTrade(createdOrder, existingOrder));

        //Date type
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        trade.setExecutionTime(timestamp);

        trade = tradeDao.addTrade(trade);
        return trade;
    }

    @Override
    public boolean cancelOrder(int orderId) throws UnexpectedOrderStateError {
        List<Order> currentOrders = orderDao.getCurrentOrders();
        List<Order> orderToBeCanceled = currentOrders.stream()
                .filter( o -> o.getOrderId() == orderId )
                .collect(Collectors.toList());

        if (orderToBeCanceled.isEmpty()) {
            throw new UnexpectedOrderStateError("This order is not in a state where it can be canceled");
        } else {
            return orderDao.cancelOrder(orderId);
        }
    }

    @Override
    public List<Order> getOrdersByClientId(int clientId){
        return orderDao.getOrdersByClientId(clientId);
    }

    @Override
    public List<Trade> getTradesByOrderId(int orderId) {
        return tradeDao.getTradesByOrderId(orderId);
    }
}
