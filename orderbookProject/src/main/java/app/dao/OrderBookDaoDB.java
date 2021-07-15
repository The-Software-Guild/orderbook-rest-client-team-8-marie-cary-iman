package app.dao;

import app.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderBookDaoDB implements OrderBookDao {

  private final JdbcTemplate jdbc;

  @Autowired
  public OrderBookDaoDB(JdbcTemplate jdbcTemplate) {
    this.jdbc = jdbcTemplate;
  }

  @Override
  public List<Order> getAllOrders() {
    final String SELECT_ALL_ORDERS = "SELECT * FROM ordertable";
    return jdbc.query(SELECT_ALL_ORDERS, new OrderMapper());
  }

  public List<Order> getAllOrders(String stockSymbol) {
    final String SELECT_ALL_ORDERS_FOR_STOCK = String.format("SELECT * FROM ordertable WHERE stockSymbol = %s", stockSymbol);
    return jdbc.query(SELECT_ALL_ORDERS_FOR_STOCK, new OrderMapper());
  }

  @Override
  public List<Order> getSellOrders(String stockSymbol) {
    final String SELECT_SELL_ORDERS = String.format("SELECT * FROM ordertable WHERE stockSymbol = '%s' AND orderType = 'sell' AND (orderStatus = 'new' OR orderStatus = 'partial') ORDER BY price ASC, orderTime DESC", stockSymbol);
    return jdbc.query(SELECT_SELL_ORDERS, new OrderMapper());
  }

  @Override
  public List<Order> getBuyOrders(String stockSymbol) {
    final String SELECT_BUY_ORDERS = String.format("SELECT * FROM ordertable WHERE stockSymbol = '%s' AND orderType = 'buy' AND (orderStatus = 'new' OR orderStatus = 'partial') ORDER BY price DESC, orderTime DESC", stockSymbol);
    return jdbc.query(SELECT_BUY_ORDERS, new OrderMapper());
  }

  @Override
  public Order getOrder(int orderId) {
    try{
      final String SELECT_ORDER_BY_ID = "SELECT * FROM ordertable WHERE orderId = ?";
      return jdbc.queryForObject(SELECT_ORDER_BY_ID, new OrderMapper(), orderId);
    } catch (DataAccessException ex) {
      return null;
    }
  }

  @Override
  public List<Order> getOrdersByClientId(int clientId) {
    System.out.println(clientId);
    try {
      final String SELECT_ORDERS_BY_CLIENT = String.format("SELECT * FROM ordertable WHERE clientId = %s ORDER BY orderTime", clientId);
      return jdbc.query(SELECT_ORDERS_BY_CLIENT, new OrderMapper());
    } catch (DataAccessException e) {
      return null;
    }
  }

  @Override
  @Transactional
  public Order addOrder(Order newOrder) {
    final String INSERT_ORDER = "INSERT INTO ordertable(clientId, orderType, orderStatus, stockSymbol, cumulativeQuantity, price) VALUES(?,?,?,?,?,?)";

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    // set orderStatus to 'new' ?
    newOrder.setOrderStatus("new");
    jdbc.update((Connection conn) -> {

      PreparedStatement statement = conn.prepareStatement(
              INSERT_ORDER,
              Statement.RETURN_GENERATED_KEYS);

      statement.setInt(1, newOrder.getClientId());
      statement.setString(2, newOrder.getOrderType());
      statement.setString(3, newOrder.getOrderStatus());
      statement.setString(4, newOrder.getStockSymbol());
      statement.setInt(5, newOrder.getCumulativeQuantity());
      statement.setBigDecimal(6, newOrder.getPrice());
      return statement;

    }, keyHolder);

    newOrder.setOrderId(keyHolder.getKey().intValue());

    return newOrder;
  }

  @Override
  public boolean updateOrder(Order order) {
    final String UPDATE_ORDER = "UPDATE ordertable SET cumulativeQuantity = ?, price = ? WHERE orderId = ?";
    return jdbc.update(UPDATE_ORDER, order.getCumulativeQuantity(), order.getPrice(), order.getOrderId()) > 0;
  }

  @Override
  public boolean deleteOrderById(int orderId) {
    final String DELETE_ORDER = "DELETE FROM ordertable WHERE orderId = ?";
    return jdbc.update(DELETE_ORDER, orderId) > 0;
  }

  @Override
  public boolean cancelOrder(int orderId) {
    final String CANCEL_ORDER = "UPDATE ordertable SET orderStatus = 'canceled' WHERE orderId = ?";
    return jdbc.update(CANCEL_ORDER, orderId) > 0;
  }

  @Override
  public List<Order> getCurrentOrders(){
    final String SELECT_CURRENT_ORDERS = "SELECT * FROM ordertable WHERE orderStatus != 'canceled' AND orderStatus != 'completed'";
    return jdbc.query(SELECT_CURRENT_ORDERS, new OrderMapper());
  }

  private static final class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int index) throws SQLException {
      Order order = new Order();
      order.setOrderId(rs.getInt("orderId"));
      order.setClientId(rs.getInt("clientId"));
      order.setStockSymbol(rs.getString("stockSymbol"));
      order.setOrderType(rs.getString("orderType"));
      order.setOrderStatus(rs.getString("orderStatus"));
      order.setCumulativeQuantity(rs.getInt("cumulativeQuantity"));
      order.setPrice(rs.getBigDecimal("price"));
      return order;
    }
  }

}
