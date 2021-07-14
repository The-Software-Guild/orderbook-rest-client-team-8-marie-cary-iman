package app.dao;

import app.dto.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

public class TradeDaoDB implements TradeDao{

    private final JdbcTemplate jdbc;

    @Autowired
    public TradeDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public List<Trade> getAllTrades() {
        final String SELECT_ALL_TRADES = "SELECT * FROM trade";
        return jdbc.query(SELECT_ALL_TRADES, new TradeMapper());
    }

    @Override
    @Transactional
    public Trade addTrade(Trade newTrade) {
        final String INSERT_TRADE = "INSERT INTO table(buyerId, buyerPrice, sellerId, sellerPrice, quantityFilled) VALUES(?,?,?,?,?)";

        GeneratedKeyHolder keyholder = new GeneratedKeyHolder();

        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_TRADE,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, newTrade.getBuyerId());
            statement.setBigDecimal(2, newTrade.getBuyerPrice());
            statement.setInt(3, newTrade.getSellerId());
            statement.setBigDecimal(4, newTrade.getSellerPrice());
            statement.setInt(5, newTrade.getQuantityFilled());
            statement.setString(6,newTrade.getStockSymbol());

            return statement;
        }, keyholder);

        newTrade.setTradeId(keyholder.getKey().intValue());
        return newTrade;
    }

    @Override
    public boolean deleteTrade(int tradeId) {
        final String DELETE_TRADE = "DELETE FROM trade WHERE tradeId = ?";
        return jdbc.update(DELETE_TRADE, tradeId) > 0;
    }

    private static final class TradeMapper implements RowMapper<Trade> {
        @Override
        public Trade mapRow(ResultSet rs, int index) throws SQLException {
            Trade trade = new Trade();
            trade.setTradeId(rs.getInt("tradeId"));
            trade.setBuyerId(rs.getInt("buyerId"));
            trade.setBuyerPrice(rs.getBigDecimal("buyerPrice"));
            trade.setSellerId(rs.getInt("sellerId"));
            trade.setSellerPrice(rs.getBigDecimal("sellerPrice"));
            trade.setQuantityFilled(rs.getInt("quantityFilled"));
            trade.setExecutionTime(rs.getTimestamp("executionTime"));
            trade.setStockSymbol(rs.getString("stockSymbol"));
            return trade;
        }
    }

}
