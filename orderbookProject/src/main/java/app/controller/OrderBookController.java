package app.controller;

import app.dao.OrderDao;
import app.dto.Order;
import app.dto.Trade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("orderbook")
public class OrderBookController {
  private final OrderDao dao;

  public OrderBookController(OrderDao dao) {
    this.dao = dao;
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
    return dao.getCurrentOrders();
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  /* Create order with status "Begin". Return 201 CREATED as well as OrderId */
  public String create(@RequestBody Order order){
    order.setOrderStatus("begin");
    dao.addOrder(order);
    System.out.println(order.getOrderStatus());
    return String.format("201 CREATED. OrderId: %d",order.getOrderId());
  }

  @PutMapping("/order/{orderId}")
  /* Updates order by passing in OrderId, ClientId, Stock symbol, side, order quantity, and price as JSON,
  verify IDs are valid and order is able to be updated (Begin, New, or Partial), then update order. Returns the OrderId and order status on success
  and order id with error message on failure.
   */
  // TODO: Custom message
  public String update(@PathVariable int orderId, @RequestBody Order order){
    String status = "404";
    String message = "";
    if(orderId != order.getOrderId()) {
      status = "404";
      message = "ERROR MESSAGE"; //TODO informative message
    } else if(dao.updateOrder(order)) {
      status = "202";
      message = "UPDATE SUCCESS";
    }
    return String.format("%s %s. OrderId: %d",status, message, orderId);
  }

  @PutMapping("/cancel/{orderId}")
  /*attempts to cancel order by passing orderId. On success, returns the OrderId and updated order status. If the order was completed/canceled, return an error containing orderId, order status, and informative
  message
   */
  public String cancel(@PathVariable int orderId) {
    String status;
    String message;
    if(dao.cancelOrder(orderId)){
      status = "201";
      message = "ORDER CANCELED";
    } else {
      status = "404";
      message = "SOME INFORMATIVE MESSAGE"; //TODO: reason for 404
    }
    return String.format("%s %s. OrderId: %d",status, message, orderId);
  }

  @GetMapping("/current")
  /* return list of all active orders (NOT completed or canceled). This should include all info of each order
   */
  public List<Order> current(){
    return dao.getCurrentOrders();
  }

  @GetMapping("/orders/{clientId}")
  //return list of orders for specified client, sorted by time.
  public List<Order> getByClientId(@PathVariable int clientId){
    List<Order> orders = dao.getOrdersByClientId(clientId);
    return orders;
  }

/*
  @GetMapping("trades/{orderId}")
  //returns each trade of a specified order based on id
  public List<Order> getTradesByOrderId(@PathVariable int id){
    List<Trade> trades = dao.geTradesByOrderId;
  }

  /* OPTIONAL
  @GetMapping("/generate")
   */

}
