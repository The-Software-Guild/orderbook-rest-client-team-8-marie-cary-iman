import app.App;
import app.TestApplicationConfiguration;
import app.dao.OrderBookDao;
import app.dao.OrderBookDaoDB;
import app.dto.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class OrderBookDaoDBTest {
  @Autowired
  OrderBookDao orderDao;

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

}
