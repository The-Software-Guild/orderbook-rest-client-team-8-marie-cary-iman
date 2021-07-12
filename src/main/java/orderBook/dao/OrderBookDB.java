package orderBook.dao;

import orderBook.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderBookDB implements OrderBookDao {
    @Autowired
    JdbcTemplate jdbc;

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

    @Override
    public List<Order> getAllOrders() {
        final String SELECT_ALL_ORDERS = "SELECT * FROM ordertable";
        return jdbc.query(SELECT_ALL_ORDERS, new OrderMapper());
    }

    public Order getOrder(int orderId) {
        try{
            final String SELECT_ORDER_BY_ID = "SELECT * FROM ordertable WHERE orderId = ?";
            return jdbc.queryForObject(SELECT_ORDER_BY_ID, new OrderMapper(), orderId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Order addOrder(Order order) {
        final String INSERT_ORDER = "INSERT INTO orders(orderType, stockSymbol, cumulativeQuantity, price) VALUES(?,?,?,?)";
        jdbc.update(INSERT_ORDER, order.getOrderType(), order.getStockSymbol(), order.getCumulativeQuantity(), order.getPrice());
        int newOrderId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        order.setOrderId(newOrderId);
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        final String UPDATE_ORDER = "UPDATE ordertable SET cumulativeQuantity = ?, price = ? WHERE orderId = ?";
        jdbc.update(UPDATE_ORDER, order.getCumulativeQuantity(), order.getPrice(), order.getOrderId());
    }

    @Override
    @Transactional
    public void cancelOrder(int orderId) {
        final String CANCEL_ORDER = "UPDATE ordertable SET orderStatus = canceled WHERE orderId = orderId";
        jdbc.update(CANCEL_ORDER);
    }

}
