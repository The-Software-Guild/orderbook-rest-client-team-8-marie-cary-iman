package app.dao;

import app.dto.Trade;

import java.util.List;

public interface TradeDao {
    List<Trade> getAllTrades();
    Trade addTrade(Trade newTrade);
    boolean deleteTrade(int tradeId);
}
