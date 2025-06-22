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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import ch.hslu.swda.api.StudentDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testcases für Student Service. Verwendet Micronaut.io.
 */
@MicronautTest
final class EndpointStudentTest {

    static final Logger LOG = LoggerFactory.getLogger(EndpointStudentTest.class);

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    public void testCreate() throws Exception {
        final HttpRequest<StudentDto> request = HttpRequest.POST("/api/v1/students", new StudentDto(0, "Roland", "Gisler", "Luzern"));
        final HttpResponse<StudentDto> response = this.client.toBlocking().exchange(request, StudentDto.class);
        assertThat(response.getBody().isPresent()).isTrue();
        final StudentDto result = response.getBody().get();
        assertAll("StudentDTO" , 
                () -> assertThat(result.getFirstName()).isEqualTo("Roland"),
                () -> assertThat(result.getLastName()).isEqualTo("Gisler"),
                () -> assertThat(result.getOrt()).isEqualTo("Luzern")
        );
    }

    @Test
    public void testGetStudentOne() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students/1");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Grone\"");
    }

    @Test
    public void testGetStudentTwo() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students/2");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Zweifel\"");
    }

    @Test
    public void testGetStudentTree() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students/3");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Kaiser\"");
    }

    @Test
    public void testStudentSearch() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students?lastname=Zweifel");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Zweifel\"");
    }
}
