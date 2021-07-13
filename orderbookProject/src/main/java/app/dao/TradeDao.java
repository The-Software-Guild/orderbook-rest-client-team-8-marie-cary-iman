package app.dao;

import app.dto.Trade;

import java.util.List;

public interface TradeDao {
    List<Trade> getAllTrade();
    Trade addTrade();
    Trade deleteTrade();
}
