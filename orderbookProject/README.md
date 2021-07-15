#Order Book API

### Completed Endpoints

---

#### Create Order : orderbook/logon  
POST request creates an order with order status "Begin".  
Returns 201 CREATED and the orderId
JSON body
``` 
{    
  "clientId" : 1,   
  "stockSymbol" : "TSLA",  
  "orderType" : "buy",  
  "orderStatus" : "New",
  "quantity" : 50,
  "price" : 37.50
}
```

---

#### Get All Orders : orderbook/all  
GET request returns a JSON with all the orders in the Order Table  


---

#### Update Order By Id : orderbook/order/{orderId}
PUT request with JSON payload.
If no order matches, returns 404 and displays an error message.
If update successful, returns 202 update success.

Example JSON payload  
```
{  
  "orderId": 1,  
  "clientId" : 1,   
  "stockSymbol" : "TSLA",  
  "orderType" : "sell",  
  "orderStatus" : "New",
  "quantity" : 50,
  "price" : 37.50
}
```

---
#### Cancel Order By Id : orderbook/cancel/{orderId}
PUT request 
If no order matches, returns 404 and displays an error message.
If update successful, returns 202 update success.

---
#### Current order  : orderbook/current
GET request
List all active order.


---
#### Client order  : orderbook/orders/{clientId}
GET request
List all order of a specified client.

---
#### Client order  : orderbook/orders/{clientId}
GET request
List all trade of a specified order.

