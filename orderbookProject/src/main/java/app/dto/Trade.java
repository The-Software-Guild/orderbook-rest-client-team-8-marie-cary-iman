package app.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

public class Trade {
  private Integer tradeId;
  private Integer buyerId;
  private BigDecimal buyerPrice;
  private Integer sellerId;
  private BigDecimal sellerPrice;
  private Integer quantityFilled;
  private Date executionTime;
  private String stockSymbol;


  public String getStockSymbol() {
    return stockSymbol;
  }

  public void setStockSymbol(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }



  public Integer getTradeId() {
    return tradeId;
  }

  public void setTradeId(Integer tradeId) {
    this.tradeId = tradeId;
  }

  public Integer getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(Integer buyerId) {
    this.buyerId = buyerId;
  }

  public BigDecimal getBuyerPrice() {
    return buyerPrice;
  }

  public void setBuyerPrice(BigDecimal buyerPrice) {
    this.buyerPrice = buyerPrice;
  }

  public Integer getSellerId() {
    return sellerId;
  }

  public void setSellerId(Integer sellerId) {
    this.sellerId = sellerId;
  }

  public BigDecimal getSellerPrice() {
    return sellerPrice;
  }

  public void setSellerPrice(BigDecimal sellerPrice) {
    this.sellerPrice = sellerPrice;
  }

  public Integer getQuantityFilled() {
    return quantityFilled;
  }

  public void setQuantityFilled(Integer quantityFilled) {
    this.quantityFilled = quantityFilled;
  }

  public Date getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(Date executionTime) {
    this.executionTime = executionTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Trade trade = (Trade)o;
    return Objects.equals(tradeId, trade.tradeId) && Objects.equals(buyerId, trade.buyerId) && Objects.equals(buyerPrice, trade.buyerPrice) && Objects.equals(sellerId, trade.sellerId) && Objects.equals(sellerPrice, trade.sellerPrice) && Objects.equals(quantityFilled, trade.quantityFilled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeId, buyerId, buyerPrice, sellerId, sellerPrice, quantityFilled);
  }
}
