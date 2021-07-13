DROP DATABASE IF EXISTS orderBookTest;
CREATE DATABASE orderBookTest;

USE orderBookTest;

CREATE TABLE client(
  clientId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40)
);

CREATE TABLE order(
  orderId INT PRIMARY KEY AUTO_INCREMENT,
  clientId INT,
  stockSymbol VARCHAR(10),
  orderType VARCHAR(10),
  orderStatus VARCHAR(40),
  cumulativeQuantity INT,
  price DECIMAL,
  CONSTRAINT fk_client_id FOREIGN KEY (clientId) REFERENCES `client`(clientId)
);

CREATE TABLE trade(
  tradeId INT PRIMARY KEY AUTO_INCREMENT,
  buyerId INT,
  buyerPrice DECIMAL,
  sellerId INT,
  sellerPrice DECIMAL,
  quantityFilled INT,
  executionTime TIMESTAMP,
  CONSTRAINT fk_buyer_id FOREIGN KEY (buyerId) REFERENCES `client`(clientId),
  CONSTRAINT fk_seller_id FOREIGN KEY (sellerId) REFERENCES `client`(clientId)
);