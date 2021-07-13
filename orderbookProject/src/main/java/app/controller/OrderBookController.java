package app.controller;

import app.dao.OrderBookDao;
import app.dto.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderBookController {
  private final OrderBookDao dao;

  public OrderBookController(OrderBookDao dao) {
    this.dao = dao;
  }

  /**
   * Fetches all active orders, ignores cancelled and completed orders.
   *
   * @return active orders.
   */
  @GetMapping
  public List<Order> all() {
    return dao.getAllOrders();
  }

}
