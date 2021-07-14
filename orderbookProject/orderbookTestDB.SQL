DROP DATABASE IF EXISTS orderBookTest;
CREATE DATABASE orderBookTest;

USE orderBookTest;

CREATE TABLE client(
  clientId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40)
);

CREATE TABLE orderTable(
  orderId INT PRIMARY KEY AUTO_INCREMENT,
  clientId INT,
  stockSymbol VARCHAR(10),
  orderType VARCHAR(10),
  orderStatus VARCHAR(40),
  cumulativeQuantity INT,
  price DECIMAL(20,10),
  orderTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_client_id FOREIGN KEY (clientId) REFERENCES `client`(clientId)
);

CREATE TABLE trade(
  tradeId INT PRIMARY KEY AUTO_INCREMENT,
  buyerId INT,
  buyerPrice DECIMAL(20,10),
  sellerId INT,
  sellerPrice DECIMAL(20,10),
  quantityFilled INT,
  executionTime TIMESTAMP,
  CONSTRAINT fk_buyer_id FOREIGN KEY (buyerId) REFERENCES `client`(clientId),
  CONSTRAINT fk_seller_id FOREIGN KEY (sellerId) REFERENCES `client`(clientId)
);