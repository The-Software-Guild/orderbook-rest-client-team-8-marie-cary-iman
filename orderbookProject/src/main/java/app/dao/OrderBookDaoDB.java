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

@Repository
public class OrderBookDaoDB implements OrderBookDao {

  private final JdbcTemplate jdbc;

  @Autowired
  public OrderBookDaoDB(JdbcTemplate jdbcTemplate) {
    this.jdbc = jdbcTemplate;
  }

  @Override
  public List<Order> getAllOrders() {
    final String SELECT_ALL_ORDERS = "SELECT * FROM orderTable";
    return jdbc.query(SELECT_ALL_ORDERS, new OrderMapper());
  }

  @Override
  public Order getOrder(int orderId) {
    try{
      final String SELECT_ORDER_BY_ID = "SELECT * FROM orderTable WHERE orderId = ?";
      return jdbc.queryForObject(SELECT_ORDER_BY_ID, new OrderMapper(), orderId);
    } catch (DataAccessException ex) {
      return null;
    }
  }

  @Override
  @Transactional
  public Order addOrder(Order newOrder) {

    final String INSERT_ORDER = "INSERT INTO orderTable(clientId, orderType, orderStatus, stockSymbol, cumulativeQuantity, price) VALUES(?,?,?,?,?,?)";

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

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
  public void updateOrder(Order order) {
    final String UPDATE_ORDER = "UPDATE orderTable SET cumulativeQuantity = ?, price = ? WHERE orderId = ?";
    jdbc.update(UPDATE_ORDER, order.getCumulativeQuantity(), order.getPrice(), order.getOrderId());
  }

  @Override
  public void cancelOrder(int orderId) {
    final String CANCEL_ORDER = "UPDATE orderTable SET orderStatus = canceled WHERE orderId = orderId";
    jdbc.update(CANCEL_ORDER);
  }

  @Override
  public boolean deleteOrderById(int orderId) {
    final String DELETE_ORDER = "DELETE FROM orderTable WHERE orderId = ?";
    return jdbc.update(DELETE_ORDER, orderId) > 0;
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
