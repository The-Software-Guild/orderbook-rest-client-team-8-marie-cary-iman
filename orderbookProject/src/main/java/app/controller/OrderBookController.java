package app.controller;

import app.dao.OrderDao;
import app.dto.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderBookController {
  private final OrderDao dao;

  public OrderBookController(OrderDao dao) {
    this.dao = dao;
  }

  @GetMapping
  public List<Order> all() {
    return dao.getAllOrders();
  }

}
