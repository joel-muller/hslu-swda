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
package ch.hslu.swda.api;

import ch.hslu.swda.api.StudentDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Testcases for {@link ch.hslu.swda.micro.api.StudentDto}.
 */
final class StudentDtoTest {

    /**
     * Test method for
     * {@link ch.hslu.swda.micro.api.StudentDto#Student(int, java.lang.String, java.lang.String, int)}.
     */
    @Test
    void testStudentConstructor() {
        final StudentDto student = new StudentDto(1, "Vorname", "Nachname", "Ort");
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(1),
                () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                () -> assertThat(student.getOrt()).isEqualTo("Ort"));
    }

    /**
     * Test method for
     * {@link ch.hslu.swda.micro.api.StudentDto#Student(String, String, int)}.
     */
    @Test
    void testStudentConstructorWithoutID() {
        final StudentDto student = new StudentDto(-1, "Vorname", "Nachname", "Ort");
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(-1),
                () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                () -> assertThat(student.getOrt()).isEqualTo("Ort"));
    }

    /**
     * Test method for {@link ch.hslu.swda.micro.api.StudentDto#Student()}.
     */
    @Test
    void testStudentConstructorDefault() {
        final StudentDto student = new StudentDto();
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(-1),
                () -> assertThat(student.getFirstName()).isEqualTo("unknown"),
                () -> assertThat(student.getLastName()).isEqualTo("unknown"),
                () -> assertThat(student.getOrt()).isEqualTo("unknown"));
    }

    /**
     * Test method for {@link ch.hslu.swda.micro.api.StudentDto#Student()}.
     */
    @Test
    void testStudentDefaultSetter() {
        final StudentDto student = new StudentDto();
        student.setId(100);
        student.setFirstName("Vorname");
        student.setLastName("Nachname");
        student.setOrt("Ort");
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(100),
                () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                () -> assertThat(student.getOrt()).isEqualTo("Ort"));
    }

    /**
     * Test method for
     * {@link ch.hslu.swda.micro.api.StudentDto#equals(java.lang.Object)}.
     */
    @Test
    void testEqualsObject() {
        EqualsVerifier.forClass(StudentDto.class).withOnlyTheseFields("id").suppress(Warning.NONFINAL_FIELDS).verify();
    }

    /**
     * Test method for {@link ch.hslu.swda.micro.api.StudentDto#toString()}.
     */
    @Test
    void testToString() {
        assertThat(new StudentDto(1, "Vorname", "Nachname", "Ort").toString())
                .contains("Vorname")
                .contains("Nachname")
                .contains("Ort");
    }
}
