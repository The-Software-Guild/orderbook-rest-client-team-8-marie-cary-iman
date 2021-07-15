package app.serviceLayer;

import app.TestApplicationConfiguration;

import app.dao.*;
import app.dto.Client;
import app.dto.Order;
import app.dto.Trade;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)

public class OrderBookServiceLayerImplTest extends TestCase {

    @Autowired
    OrderBookDao orderDao;

    @Autowired
    TradeDao tradeDao;

    @Autowired
    ClientDao clientDao;

    @Autowired
    OrderBookServiceLayer serviceLayer;



    @Before
    public void setup(){
        List<Order> orders = orderDao.getAllOrders();
        for (Order order : orders) {
            orderDao.deleteOrderById(order.getOrderId());
        }

        List<Client> clients = clientDao.getAllClients();
        for (Client client: clients) {
            clientDao.deleteClientById(client.getClientId());
        }
    }

    @Test
    public void testCheckValidOrder() {
        Client firstClient = new Client();
        firstClient.setName("Pied Piper");
        firstClient = clientDao.addClient(firstClient);

        Client secondClient = new Client();
        secondClient.setName("Hooli");
        secondClient = clientDao.addClient(secondClient);
        Order buyOrder1 = new Order();

        buyOrder1.setClientId(firstClient.getClientId());
        buyOrder1.setOrderType("buy");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);

        Order buyOrder2 = new Order();
        buyOrder2.setClientId(firstClient.getClientId());
        buyOrder2.setOrderType("buy");
        buyOrder2.setOrderStatus("New");
        buyOrder2.setStockSymbol("TSLA");
        buyOrder2.setCumulativeQuantity(50);
        buyOrder2.setPrice(new BigDecimal("19.99"));
        orderDao.addOrder(buyOrder2);

        List<Order> buyList = orderDao.getBuyOrders(buyOrder1.getStockSymbol());
        assertNotNull(buyList);
        assertEquals(2,buyList.size());

        Order sellOrder = new Order();
        sellOrder.setClientId(secondClient.getClientId());
        sellOrder.setOrderType("sell");
        sellOrder.setOrderStatus("New");
        sellOrder.setStockSymbol("TSLA");
        sellOrder.setCumulativeQuantity(25);
        sellOrder.setPrice(new BigDecimal("19.80"));


        Order coupledOrder = serviceLayer.checkValidOrder(sellOrder);

        assertNotSame(sellOrder.getOrderId(), coupledOrder.getOrderId());
        assertEquals(coupledOrder.getOrderId(), buyOrder1.getOrderId());

    }
    @Test
    public void testExecuteValidOrder() {
        Client firstClient = new Client();
        firstClient.setName("Pied Piper");
        firstClient = clientDao.addClient(firstClient);

        Client secondClient = new Client();
        secondClient.setName("Hooli");
        secondClient = clientDao.addClient(secondClient);
        Order buyOrder1 = new Order();

        buyOrder1.setClientId(firstClient.getClientId());
        buyOrder1.setOrderType("buy");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);


        Order sellOrder = new Order();
        sellOrder.setClientId(secondClient.getClientId());
        sellOrder.setOrderType("sell");
        sellOrder.setOrderStatus("New");
        sellOrder.setStockSymbol("TSLA");
        sellOrder.setCumulativeQuantity(25);
        sellOrder.setPrice(new BigDecimal("19.80"));

        serviceLayer.executeValidOrder(sellOrder,buyOrder1);
        assertSame("partial", buyOrder1.getOrderStatus());

        assertEquals("completed",sellOrder.getOrderStatus());
        assertEquals(sellOrder.getCumulativeQuantity(),0);

    }
    @Test
    public void testCreateTrade() {
        Client firstClient = new Client();
        firstClient.setName("Pied Piper");
        firstClient = clientDao.addClient(firstClient);

        Client secondClient = new Client();
        secondClient.setName("Hooli");
        secondClient = clientDao.addClient(secondClient);
        Order buyOrder1 = new Order();

        buyOrder1.setClientId(firstClient.getClientId());
        buyOrder1.setOrderType("buy");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);


        Order sellOrder = new Order();
        sellOrder.setClientId(secondClient.getClientId());
        sellOrder.setOrderType("sell");
        sellOrder.setOrderStatus("New");
        sellOrder.setStockSymbol("TSLA");
        sellOrder.setCumulativeQuantity(25);
        sellOrder.setPrice(new BigDecimal("19.80"));


        Trade trade = serviceLayer.createTrade(buyOrder1,sellOrder);
        assertNotNull(trade.getTradeId());


    }
}