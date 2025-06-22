/*
 * Copyright 2024 Roland Gisler, Hochschule Luzern - Informatik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.swda.micronaut;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Testcases für Student Service. Verwendet RestAssured und nutzt TestContainer,
 * d.h. die aktuelle Version muss als Docker-Image verfügbar sein (gebunden an
 * package-Lifecycle).
 */
@Testcontainers
final class RestAssuredStudentIT {

    private static final int PORT = 8090;
    private static final String IMAGE = "swda/swda_micro_service:latest";

    @Container
    private final GenericContainer<?> server
            = new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withStartupTimeout(Duration.ofSeconds(60))
                    .withExposedPorts(PORT);

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://" + server.getHost();
        RestAssured.port = server.getMappedPort(PORT);
        RestAssured.basePath = "/api/v1";
    }

    @Test
    void testStudentGetOne() throws Exception {
        given()
            .accept(ContentType.JSON).
        when()
            .get("students/1").
        then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("firstName", equalTo("Herbert"))
            .body("lastName", equalTo("Grone"))
            .body("ort", equalTo("Luzern"));
    }

    @Test
    void testStudentGetTwo() throws Exception {
        given()
            .accept(ContentType.JSON).
        when()
            .get("students/2").
        then()
            .statusCode(200)
            .body("id", equalTo(2))
            .body("firstName", equalTo("Babette"))
            .body("lastName", equalTo("Zweifel"))
            .body("ort", equalTo("Zürich"));
    }
}
