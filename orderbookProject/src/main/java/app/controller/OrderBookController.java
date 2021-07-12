package app.controller;

import app.dao.OrderBookDao;
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

}
