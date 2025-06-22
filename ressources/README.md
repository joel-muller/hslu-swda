# HSLU_SWDA

## Start Backbone

```shell
docker compose -f g02-backbone/stack.local.yml pull
docker compose -f g02-backbone/stack.local.yml up
docker compose -f g02-backbone/stack.local.yml up -d
docker compose -f g02-backbone/stack.local.yml down
docker compose -f g02-backbone/stack.local.yml ps -a
```

## Start Backbone Central Warehouse

```shell
docker compose -f g-02-central-warehouse-service/stack.local.yml up -d
docker compose -f g-02-central-warehouse-service/stack.local.yml down
```

```shell
docker compose -f g-02-order-service/stack.local.yml up -d
docker compose -f g-02-order-service/stack.local.yml down
```

## SSH to Server

```shell
ssh g02@rocky-linux-002.prod.swda.labservices.ch
ssh-copy-id -i .ssh/id_rsa.pub g02@rocky-linux-002.prod.swda.labservices.ch
```

## Start Service

```shell
mvn docker:build
mvn docker:start
mvn docker:logs
mvn docker:stop
```

## Run Java Jar files

```shell
java -jar g-02-customer-management-service/target/service.jar
```

```shell
java -jar g-02-log-service/target/service.jar
```

```shell
java -jar g-02-order-service/target/service.jar
```

```shell
java -jar g-02-store-management-service/target/service.jar
```

## TBD

## TODO Joel

**Order service**

- [x] Remove state because inconsistent state is maybe possibel
- [x] Write order finished for celine if she is done
- [x] Make tests for persistence layer
- [x] Make some more unit for the one that are not there
- [x] Stornierbare orders implementation
- [x] Stornierbare orders uber gateway

**Store management service**

- [x] Manuelle bestellungen von artikel in store uber gateway
- [x] Inventory update verarbeiten
- [x] Tests

## Docker commands

```shell
docker stats
```

```shell
docker logs id
docker logs 993c1934fe94 --follow
```