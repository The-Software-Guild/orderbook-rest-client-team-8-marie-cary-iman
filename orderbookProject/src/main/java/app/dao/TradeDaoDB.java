package app.dao;

import app.dto.Trade;
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
        final String INSERT_TRADE = "INSERT INTO trade(buyerId, buyerPrice, sellerId, sellerPrice, quantityFilled) VALUES(?,?,?,?,?)";

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
            return statement;
        }, keyholder);

        newTrade.setTradeId(keyholder.getKey().intValue());
        return newTrade;
    }

    @Override
    public Trade getTradeById(int tradeId) {
        try{
            final String SELECT_TRADE_BY_ID = "SELECT * FROM trade WHERE tradeId = ?";
            return jdbc.queryForObject(SELECT_TRADE_BY_ID, new TradeMapper(), tradeId);
        } catch (DataAccessException ex) {
            return null;
        }
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
            return trade;
        }
    }
}
