package app.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Order {
  private int orderId;
  private int clientId;
  private String stockSymbol;
  private String orderType;
  private String orderStatus;
  private int cumulativeQuantity;
  private BigDecimal price;
  private Timestamp timestamp;
  public int getOrderId() {
    return orderId;
  }

  public void setTimestamp(Timestamp timestamp){
    this.timestamp = timestamp;
  }

  public Timestamp getTimestamp () {
    return this.timestamp;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getClientId() {
    return clientId;
  }

  public void setClientId(int clientId) {
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

  public int getCumulativeQuantity() {
    return cumulativeQuantity;
  }

  public void setCumulativeQuantity(int quantity) {
    this.cumulativeQuantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order)o;
    return orderId == order.orderId && clientId == order.clientId && cumulativeQuantity == order.cumulativeQuantity && stockSymbol.equals(order.stockSymbol) && orderType.equals(order.orderType) && orderStatus.equals(order.orderStatus) && price.compareTo(order.price) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, clientId, stockSymbol, orderType, orderStatus, cumulativeQuantity, price);
  }

  @Override
  public String toString() {
    return "Order{" +
            "orderId=" + orderId +
            ", clientId=" + clientId +
            ", stockSymbol='" + stockSymbol + '\'' +
            ", orderType='" + orderType + '\'' +
            ", orderStatus='" + orderStatus + '\'' +
            ", cumulativeQuantity=" + cumulativeQuantity +
            ", price=" + price +
            '}';
  }
}
