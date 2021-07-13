# Order Book

### Completed Endpoints

#### Get Current Orders : orderbook/current
GET request with no payload.
Returns 200 OK and JSO with all orders that are not canceled or completed

Example JSON return
```
[{
        "orderId": 1,
        "clientId": 3,
        "stockSymbol": "MSFT",
        "orderType": "buy",
        "orderStatus": "new",
        "cumulativeQuantity": 30,
        "price": 279.7000000000,
        "timestamp": "2021-07-13T15:00:00.000+0000"
    },
    {
        "orderId": 2,
        "clientId": 3,
        "stockSymbol": "MSFT",
        "orderType": "buy",
        "orderStatus": "new",
        "cumulativeQuantity": 40,
        "price": 278.9900000000,
        "timestamp": "2021-07-13T16:00:00.000+0000"
    }, ... ]

```

---

#### Create Order : orderbook/create  
POST request creates an order with order status "Begin".  
Returns 201 CREATED and the orderId  

Example JSON return  
```
{
  "orderId" : 1
}
```

---

#### Get All Orders : orderbook/all  
GET request returns a JSON with all the orders in the Order Table  

Example JSON return  
```
[{  
  "orderId": 1,  
  "clientId" : 1,   
  "stockSymbol" : "TSLA",  
  "orderType" : "BID",  
  "orderStatus" : "New",
  "quantity" : 50,
  "price" : 37.50
},
{
  "orderId": 2,  
  "clientId" : 1,   
  "stockSymbol" : "TSLA",  
  "orderType" : "ASK",  
  "orderStatus" : "Partial",
  "quantity" : 25,
  "price" : 40.25
},
...]
```

---

#### Update Order By Id : orderbook/order/{orderId}
PUT request with JSON payload.
If no order matches, returns 404 and displays an error message.
If update successful, returns 202 update success, OrderId, and OrderStatus.

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

Example JSON return  
```
{
  "orderId" : 1.
  "orderStatus" : "New"
}
```

---



things to do:

### End Point
- "logon" - POST – Creates an Order with a status "Begin." Should return a 201 CREATED message as well as the created Order ID.
- "order" – POST – Updates an order by passing in the Order ID, Client ID, Stock Symbol, Side, Order Quantity, and Price as JSON. The program must verify the IDs are valid and that the order is able to be updated (Begin, New, or Partial), then update the order. It returns the Order ID and Order Status on success, and the Order ID with an error message on failure.
- "cancel" – POST – Attempts to cancel an order by passing in the Order ID. On success, it returns the Order ID and updated order status. If the order was already completed or canceled, it should return an error containing the Order ID, Order Status, and an informative message.
- "current" – GET – Returns a list of all active orders (NOT completed or canceled). This should include all the information of each order.
- "orders/{clientId} – GET – Returns a list of orders for the specified client, sorted by time.
- "trades/{orderId}" - GET – Returns each trade of a specific order based on ID.

### Business Logic
- test suite
- create new trade object(Logic done meg)
    - A unique Trade ID
    -  Execution time of the trade
     - Quantity filled in the trade 
    - Sell client for the trade
     - Sell price for the trade
     - Buy client for the trade
     - Buy price for the trade

- Order Matching
    - partial buy/sell(Logic done meg)
    - full buy/sell(Logic done meg)
    - commission
