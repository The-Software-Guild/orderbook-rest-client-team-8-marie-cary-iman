package app.dao;

import app.dto.Trade;

import java.util.List;

public interface TradeDao {
    List<Trade> getAllTrades();
    Trade addTrade(Trade newTrade);
    Trade getTradeById(int tradeId);
    boolean deleteTrade(int tradeId);
}
