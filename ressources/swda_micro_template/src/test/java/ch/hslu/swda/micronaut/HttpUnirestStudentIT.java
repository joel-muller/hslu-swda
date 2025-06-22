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

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.hslu.swda.api.StudentDto;
import java.time.Duration;
import kong.unirest.GenericType;
import kong.unirest.Unirest;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
/**
 * Testcases für Student Service. Verwendet Unirest und nutzt
 * TestContainer, d.h. die aktuelle Version muss als Docker-Image verfügbar
 * sein (gebunden an package-Lifecycle).
 */
@Testcontainers
final class HttpUnirestStudentIT {

    private static final int PORT = 8090;
    private static final String IMAGE = "swda/swda_micro_service:latest";
    private static final String BASE_URL = "http://%s:%s/api/v1/students";

    @Container
    GenericContainer<?> server
            = new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withStartupTimeout(Duration.ofSeconds(60))
                    .withExposedPorts(PORT);

    @BeforeAll
    static void beforeAll() {
        Unirest.config().automaticRetries(false);
    }

    @AfterAll
    static void afterAll() {
        Unirest.shutDown();
    }

    @Test
    void testStudentGetOneAsJson() throws Exception {
        final String json = Unirest.get(String.format(BASE_URL, server.getHost(), server.getMappedPort(PORT)) + "/1").asString().getBody();
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"lastName\":\"Grone\"");
    }

    @Test
    void testStudentGetTwoAsObject() throws Exception {
        final StudentDto student = Unirest.get(String.format(BASE_URL, server.getHost(), server.getMappedPort(PORT)) + "/2").asObject(StudentDto.class).getBody();
        assertThat(student.getId()).isEqualTo(2);
        assertThat(student.getLastName()).isEqualTo("Zweifel");
    }

    @Test
    void testStudentsList() throws Exception {
        final List<StudentDto> students = Unirest.get(String.format(BASE_URL, server.getHost(), server.getMappedPort(PORT))).asObject(new GenericType<List<StudentDto>>() {
        }).getBody();
        assertThat(students.size()).isEqualTo(3);
    }
}
