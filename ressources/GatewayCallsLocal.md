# Gateway calls Local

### Make order

```shell
curl -X POST http://localhost:8090/api/v1/orders \
-H "Content-Type: application/json" \
-d '{
  "articles": {
    "100016": 5,
    "101058": 3,
    "102016": 44
  },
  "storeId": "5788b739-87ca-416b-804a-db70f85d1f8c",
  "customerId": "1601ce09-a907-47ad-8665-7b4705796c69",
  "employeeId": "f9e3d909-4c94-43e1-b972-2ad519ebcfb9"
}'
```

### Cancel Order

```shell
curl -X POST http://localhost:8090/api/v1/cancelOrders \
-H "Content-Type: application/json" \
-d '{
  "orderId": "7341e496-3582-4d07-bbd1-2e292b54399e"
}'
```

### Inventory update

```shell
curl -X POST http://localhost:8090/api/v1/invUpdate \
-H "Content-Type: application/json" \
-d '{
  "articles": {
    "100016": 5,
    "102016": 44,
    "101058": 3
  },
  "orderId": "6f2be99b-4c16-4672-8d31-4d9d80025b94",
  "storeId": "f94f2a7d-5a5f-45f1-a28c-30e90776384f"
}'
```

### Internal Order

```shell
curl -X POST http://localhost:8090/api/v1/internalOrder \
-H "Content-Type: application/json" \
-d '{
  "storeId": "1601ce09-a907-47ad-8665-7b4705796c69",
  "articles": {
    "120200": 10,
    "144444": 3,
    "185256": 44
  }
}'
```

### Create store

```shell
curl -X POST http://localhost:8090/api/v1/createCreate \
-H "Content-Type: application/json" \
-d '{
  "addDefaultArticle": true
}'
```