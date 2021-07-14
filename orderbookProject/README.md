#Order Book API

### Completed Endpoints

---

#### Create Order : orderbook/create  
POST request creates an order with order status "Begin".  
Returns 201 CREATED and the orderId

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
  "orderType" : "BID",  
  "orderStatus" : "New",
  "quantity" : 50,
  "price" : 37.50
}
```

---