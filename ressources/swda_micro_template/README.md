# Modul SWDA - Layer

Beispiel-Projekt für Microservice-Architektur.

## Buildstatus [Gitlab-CI](https://gitlab.com/hslu-i1/swda/)
* __develop__ [![build status](https://gitlab.com/hslu-i1/swda/swda_micro_template/badges/develop/pipeline.svg)](https://gitlab.com/hslu-i1/swda/swda_micro_template/-/pipelines)
* __master__ [![build status](https://gitlab.com/hslu-i1/swda/swda_micro_template/badges/master/pipeline.svg)](https://gitlab.com/hslu-i1/swda/swda_micro_template/-/pipelines)

## Voraussetzungen
* Lokale Docker Installation (Empfehlung: Docker Desktop) inklusive Docker Compose.

## Lokaler Betrieb
Start mit `docker compose up -d`, stop mit `docker compose down`.

## Endpoints des Micro Service
* http://localhost:8090/ - Ping
* http://localhost:8090/api/v1/students - Student API
* http://localhost:8090/health - Health Endpoint
* http://localhost:8090/metrics - Metrics Endpoint
* http://localhost:8090/swagger-ui/ - OpenAPI Doku (Swagger)
* http://localhost:8090/stop (nur mit POST, z.B. `curl -X POST http://localhost:8090/stop`)

## Zusatzdienste Endpoints (nur mit docker Compose, offline)
* http://localhost:8500/ - Consul
* http://localhost:15672/ - RabbitMQ (swda/swda)
* http://localhost:16686/ - Jaeger Tracing Server

## Build und Test
Die Erzeugung des Docker Images ist direkt in die package-Lifecycle integriert.
Der MicroService wird mittels https://www.testcontainers.org/ als Image mit
Integrationstests getestet (ggf. -DskipITs wenn nicht gewünscht).
