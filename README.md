# Order Book

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