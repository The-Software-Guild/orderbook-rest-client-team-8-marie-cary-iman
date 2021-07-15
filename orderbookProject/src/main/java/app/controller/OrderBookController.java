package app.controller;

import app.dao.OrderBookDao;
import app.dao.TradeDao;
import app.dto.Order;
import app.dto.Trade;
import app.serviceLayer.OrderBookServiceLayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orderbook")
public class OrderBookController {
  private final OrderBookDao orderDao;
  private final TradeDao tradeDao;
  private final OrderBookServiceLayer service;

  public OrderBookController(OrderBookDao orderDao, TradeDao tradeDao, OrderBookServiceLayer service) {
    this.orderDao = orderDao;
    this.tradeDao = tradeDao;
    this.service = service;
  }

  /**
   * Mapped to GET requests at /api/current.
   * Fetches all active orders, ignores cancelled and completed orders.
   *
   * @return active orders.
   */
  @RequestMapping("/all")
  @GetMapping
  public List<Order> all() {
    return orderDao.getCurrentOrders();
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  /* Create order with status "Begin". Return 201 CREATED as well as OrderId */
  public String create(@RequestBody Order order){
    order.setOrderStatus("begin");
    orderDao.addOrder(order);
    Order matchedOrder = service.checkValidOrder(order);
    if(matchedOrder != order) {
      if(order.getOrderType().equals("sell")){
        service.executeValidOrder(order, matchedOrder);
      } else if(order.getOrderType().equals("buy")) {
        service.executeValidOrder(matchedOrder, order);
      }
    }
    return String.format("201 CREATED. OrderId: %d",order.getOrderId());
  }

  @PutMapping("/order/{orderId}")
  /* Updates order by passing in OrderId, ClientId, Stock symbol, side, order quantity, and price as JSON,
  verify IDs are valid and order is able to be updated (Begin, New, or Partial), then update order. Returns the OrderId and order status on success
  and order id with error message on failure.
   */
  public String update(@PathVariable int orderId, @RequestBody Order order){
    String status = "404";
    String message = "";
    if(orderId != order.getOrderId()) {
      status = "404";
      message = "Path variable does not match orderId in body!"; //TODO informative message
    } else if (orderDao.getOrder(orderId) == null) {
      status  = "404";
      message = "ORDER ID NOT FOUND IN DATABASE";
    } else if(orderDao.updateOrder(order)) {
      status = "202";
      message = "UPDATE SUCCESS";
     // try matching orders after update
      Order matchedOrder = service.checkValidOrder(order);
      if(matchedOrder != order) {
        if(order.getOrderType().equals("sell")){
          service.executeValidOrder(order, matchedOrder);
        } else if(order.getOrderType().equals("buy")) {
          service.executeValidOrder(matchedOrder, order);
        }
      }
    }
    return String.format("%s %s. OrderId: %d",status, message, orderId);
  }

  @PutMapping("/cancel/{orderId}")
  /*attempts to cancel order by passing orderId. On success, returns the OrderId and updated order status. If the order was completed/canceled, return an error containing orderId, order status, and informative
  message
   */
  public String cancel(@PathVariable int orderId) {
    String status;
    String message = "";
    if(orderDao.cancelOrder(orderId)){
      status = "201";
      message = "ORDER CANCELED";
    } else {
      status = "404";
      if(orderDao.getOrder(orderId).getOrderStatus().equals("canceled")) {
        message = "ORDER HAS ALREADY BEEN CANCELED";
      } else if (orderDao.getOrder(orderId).getOrderStatus().equals("completed")) {
        message = "ORDER HAS ALREADY BEEN COMPLETED";
      }
    }
    return String.format("%s %s. OrderId: %d",status, message, orderId);
  }

  @GetMapping("/current")
  /* return list of all active orders (NOT completed or canceled). This should include all info of each order
   */
  public List<Order> current(){
    return orderDao.getCurrentOrders();
  }

  @GetMapping("/orders/{clientId}")
  //return list of orders for specified client, sorted by time.
  public List<Order> getByClientId(@PathVariable int clientId){
    List<Order> orders = orderDao.getOrdersByClientId(clientId);
    return orders;
  }


  @GetMapping("trades/{orderId}")
  //returns each trade of a specified order based on id
  public List<Trade> getTradesByOrderId(@PathVariable int id){
    List<Trade> trades = tradeDao.getTradesByOrderId(id);
    return trades;
  }

  /* OPTIONAL
  @GetMapping("/generate")
   */

}
