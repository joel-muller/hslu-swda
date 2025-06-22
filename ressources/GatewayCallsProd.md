# Gateway calls Prod

### Make order

```shell
curl -X POST https://gw.rocky-linux-002.prod.swda.labservices.ch/api/v1/orders \
-H "Content-Type: application/json" \
-d '{
  "articles": {
    "100016": 5,
    "101058": 3,
    "102016": 7
  },
  "storeId": "d9c2abf4-c6c8-4d82-8580-4cf1f15f4dee",
  "customerId": "1601ce09-a907-47ad-8665-7b4705796c69",
  "employeeId": "f9e3d909-4c94-43e1-b972-2ad519ebcfb9"
}'
```

### Cancel Order

```shell
curl -X POST https://gw.rocky-linux-002.prod.swda.labservices.ch/api/v1/cancelOrders \
-H "Content-Type: application/json" \
-d '{
  "orderId": "201f375f-1a66-413f-adf5-b4d829854433"
}'
```

### Inventory update

```shell
curl -X POST https://gw.rocky-linux-002.prod.swda.labservices.ch/api/v1/invUpdate \
-H "Content-Type: application/json" \
-d '{
  "articles": {
    "100016": 5,
    "102016": 44,
    "101058": 56
  },
  "orderId": "31655670-63f4-4951-aea0-a812f6789907",
  "storeId": "d9c2abf4-c6c8-4d82-8580-4cf1f15f4dee"
}'
```

### Internal Order

```shell
curl -X POST https://gw.rocky-linux-002.prod.swda.labservices.ch/api/v1/internalOrder \
-H "Content-Type: application/json" \
-d '{
  "storeId": "d9c2abf4-c6c8-4d82-8580-4cf1f15f4dee",
  "articles": {
    "103698": 10,
    "144444": 3,
    "185256": 44
  }
}'
```

### Create store

```shell
curl -X POST https://gw.rocky-linux-002.prod.swda.labservices.ch/api/v1/createCreate \
-H "Content-Type: application/json" \
-d '{
  "addDefaultArticle": true
}'
```