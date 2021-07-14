import app.TestApplicationConfiguration;

import app.dao.ClientDao;
import app.dao.OrderBookDao;
import app.dao.TradeDao;
import app.dto.Client;
import app.dto.Order;

import app.dto.Trade;
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

  @Autowired
  TradeDao tradeDao;

  @Autowired
  ClientDao clientDao;

  @Before
  public void setUp() {
    List<Trade> trades = tradeDao.getAllTrades();
    List<Order> orders = orderDao.getAllOrders();
    List<Client> clients = clientDao.getAllClients();

    for (Trade trade: trades) {
      tradeDao.deleteTrade(trade.getTradeId());
    }
    for (Order order : orders) {
      orderDao.deleteOrderById(order.getOrderId());
    }
    for (Client client: clients) {
      clientDao.deleteClientById(client.getClientId());
    }

  }

  // Client DAO testing
  @Test
  public void testAddGetClients() {
    Client firstClient = new Client();
    firstClient.setName("Pied Piper");
    firstClient = clientDao.addClient(firstClient);

    Client firstClientDao = clientDao.getClientById(firstClient.getClientId());

    assertNotNull(firstClientDao);
    assertEquals(firstClientDao, firstClient);
  }

  @Test
  public void testDeleteAndGetAllClients() {
    Client firstClient = new Client();
    firstClient.setName("Pied Piper");
    firstClient = clientDao.addClient(firstClient);

    Client secondClient = new Client();
    secondClient.setName("Hooli");
    secondClient = clientDao.addClient(secondClient);

    List<Client> allClients = clientDao.getAllClients();

    assertNotNull(allClients);
    assertEquals(2, allClients.size());
    assertTrue(allClients.contains(firstClient));
    assertTrue(allClients.contains(secondClient));

    clientDao.deleteClientById(firstClient.getClientId());
    allClients = clientDao.getAllClients();

    assertNotNull(allClients);
    assertEquals(1, allClients.size());
    assertFalse(allClients.contains(firstClient));
    assertTrue(allClients.contains(secondClient));
  }

  // Order DAO testing
  @Test
  public void testAddGetOrder() {
    Client firstClient = new Client();
    firstClient.setName("Pied Piper");
    firstClient = clientDao.addClient(firstClient);

    Order order = new Order();
    order.setClientId(firstClient.getClientId());
    order.setOrderType("buy");
    order.setOrderStatus("new");
    order.setStockSymbol("TSLA");
    order.setCumulativeQuantity(50);
    order.setPrice(new BigDecimal("20.00"));
    order = orderDao.addOrder(order);

    Order fromDao = orderDao.getOrder(order.getOrderId());

    assertNotNull(fromDao);
    assertEquals(order, fromDao);
  }

  @Test
  public void testGetAllOrders() {
    Client client = new Client();
    client.setName("Pied Piper");
    client = clientDao.addClient(client);

    // Add two new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(client.getClientId());
    firstOrder.setOrderType("buy");
    firstOrder.setOrderStatus("new");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(client.getClientId());
    secondOrder.setOrderType("sell");
    secondOrder.setOrderStatus("canceled");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    List<Order> allOrders = orderDao.getAllOrders();

    assertEquals(2, allOrders.size());
    assertTrue(allOrders.contains(firstOrder));
    assertTrue(allOrders.contains(secondOrder));
  }

  @Test
  public void testGetSellAndBuyOrder() {
    Client client = new Client();
    client.setName("Pied Piper");
    client = clientDao.addClient(client);

    Order firstOrder = new Order();
    firstOrder.setClientId(client.getClientId());
    firstOrder.setOrderType("buy");
    firstOrder.setOrderStatus("new");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(client.getClientId());
    secondOrder.setOrderType("sell");
    secondOrder.setOrderStatus("new");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    Order thirdOrder = new Order();
    thirdOrder.setClientId(client.getClientId());
    thirdOrder.setOrderType("sell");
    thirdOrder.setOrderStatus("canceled");
    thirdOrder.setStockSymbol("TSLA");
    thirdOrder.setCumulativeQuantity(50);
    thirdOrder.setPrice(new BigDecimal("20.00"));
    thirdOrder = orderDao.addOrder(thirdOrder);

    List<Order> buyOrders = orderDao.getBuyOrders(firstOrder.getStockSymbol());
    List<Order> sellOrders = orderDao.getSellOrders(secondOrder.getStockSymbol());

    assertNotNull(buyOrders);
    assertNotNull(sellOrders);
    assertEquals(1, buyOrders.size());
    assertEquals(1, sellOrders.size());
    assertFalse(buyOrders.containsAll(sellOrders));
    assertTrue(buyOrders.contains(firstOrder));
    assertTrue(sellOrders.contains(secondOrder));
    assertFalse(sellOrders.contains(thirdOrder));
  }

  @Test
  public void testCancelAndGetCurrentOrders() {
    Client client = new Client();
    client.setName("Pied Piper");
    client = clientDao.addClient(client);

    // Add two new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(client.getClientId());
    firstOrder.setOrderType("buy");
    firstOrder.setOrderStatus("new");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(client.getClientId());
    secondOrder.setOrderType("sell");
    secondOrder.setOrderStatus("new");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    List<Order> currentOrders = orderDao.getCurrentOrders();

    assertNotNull(currentOrders);
    assertEquals(2, currentOrders.size());
    assertTrue(currentOrders.contains(firstOrder));
    assertTrue(currentOrders.contains(secondOrder));

    orderDao.cancelOrder(secondOrder.getOrderId());
    currentOrders = orderDao.getCurrentOrders();

    assertNotNull(currentOrders);
    assertEquals(1, currentOrders.size());
    assertTrue(currentOrders.contains(firstOrder));
    assertFalse(currentOrders.contains(secondOrder));
  }

  @Test
  public void getOrdersByClientId() {
    // Add clients
    Client firstClient = new Client();
    firstClient.setName("Pied Piper");
    firstClient = clientDao.addClient(firstClient);

    Client secondClient = new Client();
    secondClient.setName("Hooli");
    secondClient = clientDao.addClient(secondClient);

    // Add three new orders to the database
    Order firstOrder = new Order();
    firstOrder.setClientId(firstClient.getClientId());
    firstOrder.setOrderType("buy");
    firstOrder.setOrderStatus("new");
    firstOrder.setStockSymbol("TSLA");
    firstOrder.setCumulativeQuantity(50);
    firstOrder.setPrice(new BigDecimal("20.00"));
    firstOrder = orderDao.addOrder(firstOrder);

    Order secondOrder = new Order();
    secondOrder.setClientId(secondClient.getClientId());
    secondOrder.setOrderType("buy");
    secondOrder.setOrderStatus("new");
    secondOrder.setStockSymbol("TSLA");
    secondOrder.setCumulativeQuantity(35);
    secondOrder.setPrice(new BigDecimal("35.25"));
    secondOrder = orderDao.addOrder(secondOrder);

    Order thirdOrder = new Order();
    thirdOrder.setClientId(firstClient.getClientId());
    thirdOrder.setOrderType("sell");
    thirdOrder.setOrderStatus("new");
    thirdOrder.setStockSymbol("TSLA");
    thirdOrder.setCumulativeQuantity(50);
    thirdOrder.setPrice(new BigDecimal("20.00"));
    thirdOrder = orderDao.addOrder(thirdOrder);

    // Method being tested
    List<Order> firstClientOrders = orderDao.getOrdersByClientId(firstClient.getClientId());

    // Assertions
    assertNotNull(firstClientOrders);
    assertEquals(2, firstClientOrders.size());
    assertTrue(firstClientOrders.contains(firstOrder));
    assertFalse(firstClientOrders.contains(secondOrder));
    assertTrue(firstClientOrders.contains(thirdOrder));
  }

  @Test
  public void testUpdateOrder() {
    Client firstClient = new Client();
    firstClient.setName("Pied Piper");
    firstClient = clientDao.addClient(firstClient);

    Order originalOrder = new Order();
    originalOrder.setClientId(firstClient.getClientId());
    originalOrder.setOrderType("buy");
    originalOrder.setOrderStatus("new");
    originalOrder.setStockSymbol("TSLA");
    originalOrder.setCumulativeQuantity(50);
    originalOrder.setPrice(new BigDecimal("20.00"));
    originalOrder = orderDao.addOrder(originalOrder);

    // Addition of the original order id to simulate it being the same order
    // but updated
    Order updatedOrder = new Order();
    updatedOrder.setOrderId(originalOrder.getOrderId());
    updatedOrder.setClientId(firstClient.getClientId());
    updatedOrder.setOrderType("buy");
    updatedOrder.setOrderStatus("new");
    updatedOrder.setStockSymbol("TSLA");
    updatedOrder.setCumulativeQuantity(25);
    updatedOrder.setPrice(new BigDecimal("20.00"));

    orderDao.updateOrder(updatedOrder);
    List<Order> allOrders = orderDao.getAllOrders();

    assertNotNull(allOrders);
    assertEquals(1, allOrders.size());
    assertTrue(allOrders.contains(updatedOrder));
    assertFalse(allOrders.contains(originalOrder));
  }

  // Trade DAO testing
  @Test
  public void testAddGetTrade() {
    Client client = new Client();
    client.setName("Pied Piper");
    client = clientDao.addClient(client);

    Order buyOrder = new Order();
    buyOrder.setClientId(client.getClientId());
    buyOrder.setOrderType("buy");
    buyOrder.setOrderStatus("new");
    buyOrder.setStockSymbol("TSLA");
    buyOrder.setCumulativeQuantity(50);
    buyOrder.setPrice(new BigDecimal("20.00"));
    buyOrder = orderDao.addOrder(buyOrder);

    Order sellOrder = new Order();
    sellOrder.setClientId(client.getClientId());
    sellOrder.setOrderType("sell");
    sellOrder.setOrderStatus("new");
    sellOrder.setStockSymbol("TSLA");
    sellOrder.setCumulativeQuantity(50);
    sellOrder.setPrice(new BigDecimal("20.00"));
    sellOrder = orderDao.addOrder(sellOrder);

    //Grab orders from Dao to assure not a value mismatch from the database
    sellOrder = orderDao.getOrder(sellOrder.getOrderId());
    buyOrder = orderDao.getOrder(buyOrder.getOrderId());

    Trade newTrade = new Trade();
    newTrade.setSellerId(sellOrder.getClientId());
    newTrade.setSellerPrice(sellOrder.getPrice());
    newTrade.setBuyerId(buyOrder.getClientId());
    newTrade.setBuyerPrice(buyOrder.getPrice());
    newTrade.setQuantityFilled(Math.min(sellOrder.getCumulativeQuantity(), buyOrder.getCumulativeQuantity()));
    newTrade = tradeDao.addTrade(newTrade);

    Trade tradeFromDao = tradeDao.getTradeById(newTrade.getTradeId());

    assertNotNull(tradeFromDao);
    assertEquals(newTrade, tradeFromDao);
  }

  @Test
  public void testGetAllTradesAndRemove() {
    Client client = new Client();
    client.setName("Pied Piper");
    client = clientDao.addClient(client);

    Order buyOrder = new Order();
    buyOrder.setClientId(client.getClientId());
    buyOrder.setOrderType("buy");
    buyOrder.setOrderStatus("new");
    buyOrder.setStockSymbol("TSLA");
    buyOrder.setCumulativeQuantity(50);
    buyOrder.setPrice(new BigDecimal("20.00"));
    buyOrder = orderDao.addOrder(buyOrder);

    Order sellOrder = new Order();
    sellOrder.setClientId(client.getClientId());
    sellOrder.setOrderType("sell");
    sellOrder.setOrderStatus("new");
    sellOrder.setStockSymbol("TSLA");
    sellOrder.setCumulativeQuantity(50);
    sellOrder.setPrice(new BigDecimal("20.00"));
    sellOrder = orderDao.addOrder(sellOrder);

    //Grab orders from Dao to assure not a value mismatch from the database
    sellOrder = orderDao.getOrder(sellOrder.getOrderId());
    buyOrder = orderDao.getOrder(buyOrder.getOrderId());

    Trade newTrade = new Trade();
    newTrade.setSellerId(sellOrder.getClientId());
    newTrade.setSellerPrice(sellOrder.getPrice());
    newTrade.setBuyerId(buyOrder.getClientId());
    newTrade.setBuyerPrice(buyOrder.getPrice());
    newTrade.setQuantityFilled(Math.min(sellOrder.getCumulativeQuantity(), buyOrder.getCumulativeQuantity()));
    newTrade = tradeDao.addTrade(newTrade);

    List<Trade> allTrades = tradeDao.getAllTrades();

    assertNotNull(allTrades);
    assertEquals(1, allTrades.size());
    assertTrue(allTrades.contains(newTrade));

    tradeDao.deleteTrade(newTrade.getTradeId());
    allTrades = tradeDao.getAllTrades();
    assertNotNull(allTrades);
    assertEquals(0, allTrades.size());
  }

}
