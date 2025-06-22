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

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.swda.api.StudentDto;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Testcases für Student Service. Verwendet Java 17 HttpClient und nutzt
 * TestContainer, d.h. die aktuelle Version muss als Docker-Image verfügbar
 * sein (gebunden an package-Lifecycle).
 */
@Testcontainers
final class HttpClientJavaStudentIT {

    private static final int PORT = 8090;
    private static final String IMAGE = "swda/swda_micro_service:latest";
    private static final String BASE_URL = "http://%s:%s/api/v1/students";

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

    @Container
    GenericContainer<?> server
            = new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withStartupTimeout(Duration.ofSeconds(60))
                    .withExposedPorts(PORT);

    @Test
    void testStudentGetOne() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort()) + "/1")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"id\":1");
        assertThat(response.body()).contains("\"lastName\":\"Grone\"");
    }

    @Test
    void testStudentGetTwoAsJson() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort()) + "/2")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        final String json = response.body();
        final StudentDto student = mapper.readValue(json, StudentDto.class);
        assertThat(student.getId()).isEqualTo(2);
        assertThat(student.getFirstName()).isEqualTo("Babette");
        assertThat(student.getLastName()).isEqualTo("Zweifel");
        assertThat(student.getOrt()).isEqualTo("Zürich");
    }

    @Test
    void testStudentGetWhichNotExist() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort()) + "/999999")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    void testStudentGetByLastName() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort()) + "/?lastname=Zweifel"))
                    .GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"lastName\":\"Zweifel\"");
    }

    @Test
    void testStudentDelete() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort()) + "/3")).DELETE().build();
        final HttpResponse<?> response = httpClient.send(request, BodyHandlers.discarding());
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void testStudentCreate() throws Exception {
        final StudentDto student = new StudentDto(-1L, "Vorname", "Nachname", "Ort");
        final String jsonString = mapper.writeValueAsString(student);
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(BASE_URL, server.getHost(), server.getFirstMappedPort())))
                    .header("content-type", "application/json")
                    .POST(BodyPublishers.ofString(jsonString))
                    .build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        final String json = response.body();
        final StudentDto created = mapper.readValue(json, StudentDto.class);
        assertThat(created.getId()).isGreaterThan(0);
    }
}
