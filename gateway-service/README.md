# Modul SWDA - Gateway Microservice Sample
Beispiel (Gateway-)Microservice für SWDA, basierend auf [micronaut.io](https://micronaut.io/) für g99.
* Der Service nutzt [Logback](http://logback.qos.ch/) über die [SLF4J](http://www.slf4j.org/)-
  Schnittstelle für Logging.
* Im Package `ch.hslu.swda.entities` befindet sich das (sehr kleine) Domain Model.
* Im Package `ch.hslu.swda.business` befindet sich eine klassische class-based API
  mit Interface und einer einfachen (Fake-)Implementation (in Memory Store).
* Im Package `ch.hslu.swda.micro` befindet sich der REST-Controller und der 
  Applikationskontext für das [Micronaut](https://micronaut.io/) -Framework.

### Build
* `mvn package` erstellt ein Single-JAR (enthält alle Dependencies) mit dem Namen
`service.jar` (im `./target-Verzeichnis`). 

### Start lokal (ohne Docker)
* Das Single-JAR kann direkt gestartet werden mit: `java -jar service.jar`
* Nach dem Start steht die API unter http://localhost:8090/api/v1/students/ 
  zur Verfügung.

### Docker Build (nativ)
* Erstellen des Docker-Images: `docker build . -t "swda/gateway-service"`
* Starten des Containers: `docker run -d --name swda-gateway -p 8090:8090 swda/gateway-service`
* Logs verfolgen (tail): `docker logs swda-container`
* Stoppen des Containers: `docker stop swda-container`
Hinweis: Passen Sie das Portmapping wenn nötig an.

### Docker über Maven-Plugin (alternativ)
* Erstellen der Docker-Images: `mvn docker:build`
* Starten des Containers: `mvn docker:start`
* Logs anzeigen: `mvn docker:logs`
* Stoppen des Containers: `mvn docker:stop`
Hinweis: Maven entfernt beim Stoppen auch gleich den Container.


## Curl commands crucial for demo on friday

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
  "storeId": "a7f6c44a-2f5d-4d47-aa63-b7c17ab17961",
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
    "102016": 856487,
    "101058": 356688
  },
  "orderId": "bae3be43-2655-4bbb-86be-d6213e66b1d3",
  "storeId": "a7f6c44a-2f5d-4d47-aa63-b7c17ab17961"
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
