package orderBook.dto;

import java.math.BigDecimal;

public class OrderTable {
    private Integer orderId;
    private Integer clientId;
    private String stockSymbol;
    private String orderType;
    private String orderStatus;
    private Integer cumulativeQuantity;
    private BigDecimal price;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getCumulativeQuantity() {
        return cumulativeQuantity;
    }

    public void setCumulativeQuantity(Integer cumulativeQuantity) {
        this.cumulativeQuantity = cumulativeQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
