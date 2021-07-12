package orderBook.dto;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;

public class Trade {
    private Integer tradeId;
    private Integer buyerId;
    private BigDecimal buyerPrice;
    private Integer sellerId;
    private BigDecimal sellerPrice;
    private Integer quantityFilled;
    private Date executionTime;

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
}
