import app.TestApplicationConfiguration;
import app.dao.OrderBookDao;
import app.dto.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class OrderBookDaoDBTest {
  @Autowired
  OrderBookDao orderDao;

  /**
   * TO BE REFACTORED AS DAOS ARE ADDED
   * These test methods have hard coded clientIds that need to be created in the database before running
   */

  @Before
  public void setUp() {
    List<Order> orders = orderDao.getAllOrders();
    for (Order order : orders) {
      orderDao.deleteOrderById(order.getOrderId());
    }
  }

  @Test
  public void testAddGetOrder() {
    Order order = new Order();
    order.setClientId(1);
    order.setOrderType("BID");
    order.setOrderStatus("New");
    order.setStockSymbol("TSLA");
    order.setCumulativeQuantity(50);
    order.setPrice(new BigDecimal("20.00"));
    order = orderDao.addOrder(order);

    Order fromDao = orderDao.getOrder(order.getOrderId());

    assertEquals(order, fromDao);
  }

  @Test
  public void testGetAllOrders() {
    // Add two new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(1);
    firstOrder.setOrderType("BID");
    firstOrder.setOrderStatus("New");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(1);
    secondOrder.setOrderType("ASK");
    secondOrder.setOrderStatus("Canceled");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    Order firstFromDao = orderDao.getOrder(firstOrder.getOrderId());
    Order secondFromDao = orderDao.getOrder(secondOrder.getOrderId());

    List<Order> allOrders = orderDao.getAllOrders();

    assertEquals(2, allOrders.size());
    assertTrue(allOrders.contains(firstFromDao));
    assertTrue(allOrders.contains(secondFromDao));
  }

  @Test
  public void testCancelAndGetCurrentOrders() {
    // Add two new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(1);
    firstOrder.setOrderType("BID");
    firstOrder.setOrderStatus("New");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(1);
    secondOrder.setOrderType("ASK");
    secondOrder.setOrderStatus("New");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    Order firstFromDao = orderDao.getOrder(firstOrder.getOrderId());
    Order secondFromDao = orderDao.getOrder(secondOrder.getOrderId());

    List<Order> currentOrders = orderDao.getCurrentOrders();

    assertNotNull(currentOrders);
    assertEquals(2, currentOrders.size());
    assertTrue(currentOrders.contains(firstFromDao));
    assertTrue(currentOrders.contains(secondFromDao));

    orderDao.cancelOrder(secondFromDao.getOrderId());
    currentOrders = orderDao.getCurrentOrders();

    assertNotNull(currentOrders);
    assertEquals(1, currentOrders.size());
    assertTrue(currentOrders.contains(firstFromDao));
    assertFalse(currentOrders.contains(secondFromDao));
  }

  @Test
  public void getOrdersByClientId() {
    // Add two new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(1);
    firstOrder.setOrderType("BID");
    firstOrder.setOrderStatus("New");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(2);
    secondOrder.setOrderType("ASK");
    secondOrder.setOrderStatus("New");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    Order firstFromDao = orderDao.getOrder(firstOrder.getOrderId());
    Order secondFromDao = orderDao.getOrder(secondOrder.getOrderId());

    List<Order> firstClient = orderDao.getOrdersByClientId(1);
    List<Order> secondClient = orderDao.getOrdersByClientId(2);

    assertNotNull(firstClient);
    assertNotNull(secondClient);
    assertEquals(1, firstClient.size());
    assertEquals(1, secondClient.size());
    assertTrue(firstClient.contains(firstFromDao));
    assertFalse(firstClient.contains(secondFromDao));
    assertTrue(secondClient.contains(secondFromDao));
    assertFalse(secondClient.contains(firstFromDao));
  }


}
