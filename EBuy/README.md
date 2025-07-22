## Products service API

The whole API can be started using docker with the docker compose up command

### The design
#### API-Gateway
The API consists of a microservice architecture with an API-gateway which
also doubles as a Authentication service. For all routes /order and for the route /products/new you need an API key. Rest of the routes don't need any headers.

#### Products-Service
The actual service consists of the route GET /products which returns a json with all products. There is also GET /products/search?query={SOME_QUERY} where you can serach products by their name.
You can add new products aswell with POST /products/new, but as mentioned before, you need an API key so the service knows which user is the seller

#### Order-Service
The actual EBuy part of the API. API key is needed for every route. You can make a new order with POST /order and check your other status with GET /order/status/{given_order_id}

### Connectivity between the services
All the services stated above have their own MariaDB databases.
The only open service(except Prometheus and Grafana) is the API-gateway which takes the request and forwards it to the needed service. Order and Products service are connected by a 2-way RabbitMQ Queue in which they send needed data.
The design of the queue is the following; when someone makes an order, the order service sends an event to products service and products service checks whether the stock in its Database and sends back a different event to either say it reduced the stock and
to tell Order service to put the transaction as completed or tell it the order got rejected. All the services also have Prometheus and Grafana for monitoring

### HTTP requests explained more in depth
#### GET /products
Needs no body or authentication, returns a list of products
#### GET /products/search?query={SEARCH_QUERY}
Needs no body or authentication, returns a list of all products where the product name has the search query in it
#### POST /products/new
The route needs an API-key with the header key "Authorization". The API key value is of the format "App {YOUR_API_KEY}". The body is of format
<pre markdown="1">{
    "name":product_name,
    "price": product_price,
    "description": product_description,
    "quantity": product_quantity
} </pre>
Returns the product you just made
#### POST /order
The route needs an API-key with the header key "Authorization".The API key value is of format "App {YOUR_API_KEY}". The body is of format
<pre markdown="1">{
    "id":product_id,
    "amountBought": product_amountBought,
    "rating": your_rating
} </pre>
It will return the created order.
#### GET /order/myOrders
The route needs an API-key with the header key "Authorization". The API key value is of format "App {YOUR_API_KEY}". It returns all orders made by the user associated with the API key

#### GET /order/status/{id}
The route needs an API-key with the header key "Authorization".The API key value is of format "App {YOUR_API_KEY}".
The PathVariable is the id which you are given after making an order.
The response will be a JSON with "status" set either to "PENDING","CREATED","REJECTED"