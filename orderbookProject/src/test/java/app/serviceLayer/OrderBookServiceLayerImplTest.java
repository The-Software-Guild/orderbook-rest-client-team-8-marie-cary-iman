package app.serviceLayer;

import app.TestApplicationConfiguration;

import app.dao.OrderBookDaoDB;
import app.dto.Order;
import app.dto.Trade;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)

public class OrderBookServiceLayerImplTest extends TestCase {

    @Autowired
    OrderBookDaoDB orderDao;
    OrderBookServiceLayerImpl serviceLayer;
    @Before
    public void setup(){

            List<Order> orders = orderDao.getAllOrders();
            for (Order order : orders) {
                orderDao.deleteOrderById(order.getOrderId());
            }
            serviceLayer = new OrderBookServiceLayerImpl(orderDao);

    }
    @Test
    public void testCheckValidOrder() {

        Order buyOrder1 = new Order();

        buyOrder1.setClientId(1);
        buyOrder1.setOrderType("BID");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);

        Order buyOrder2 = new Order();
        buyOrder2.setClientId(1);
        buyOrder2.setOrderType("BID");
        buyOrder2.setOrderStatus("New");
        buyOrder2.setStockSymbol("TSLA");
        buyOrder2.setCumulativeQuantity(50);
        buyOrder2.setPrice(new BigDecimal("19.99"));
        orderDao.addOrder(buyOrder2);
        List<Order> buyList = orderDao.getBuyOrders();
        assertNotNull(buyList);
        assertEquals(2,buyList.size());

        Order sellOrder = new Order();
        sellOrder.setClientId(2);
        sellOrder.setOrderType("ASK");
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
        Order buyOrder1 = new Order();

        buyOrder1.setClientId(1);
        buyOrder1.setOrderType("BID");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);


        Order sellOrder = new Order();
        sellOrder.setClientId(2);
        sellOrder.setOrderType("ASK");
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
        Order buyOrder1 = new Order();

        buyOrder1.setClientId(1);
        buyOrder1.setOrderType("BID");
        buyOrder1.setOrderStatus("New");
        buyOrder1.setStockSymbol("TSLA");
        buyOrder1.setCumulativeQuantity(50);
        buyOrder1.setPrice(new BigDecimal("20.00"));
        orderDao.addOrder(buyOrder1);


        Order sellOrder = new Order();
        sellOrder.setClientId(2);
        sellOrder.setOrderType("ASK");
        sellOrder.setOrderStatus("New");
        sellOrder.setStockSymbol("TSLA");
        sellOrder.setCumulativeQuantity(25);
        sellOrder.setPrice(new BigDecimal("19.80"));


        Trade trade = serviceLayer.createTrade(buyOrder1,sellOrder,25);
        assertNotNull(trade.getTradeId());


    }
}