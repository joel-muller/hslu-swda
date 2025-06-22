/*
 * Copyright 2024 Roland Gisler, HSLU Informatik, Switzerland
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

import java.util.List;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;

import ch.hslu.swda.api.StudentDto;
import ch.hslu.swda.api.Students;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller für Students-API.
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/api/v1/students")
public class StudentController {

    private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

    @Inject
    private Students students;

    /**
     * Alle Studierende anzeigen oder nach Name suchen.
     *
     * @param lastName Name des gesuchten Studierenden.
     * @return Liste von Studierenden (StudentDto).
     */
    @Tag(name = "Students")
    @Get("/{?lastname}")
    public List<StudentDto> search(@QueryValue("lastname") @Nullable final String lastName) {
        final List<StudentDto> result;
        if (lastName != null) {
            result = students.findByLastName(lastName);
            LOG.info("REST: Liste mit {} Studierenden mit Name '{}' geliefert.", result.size(), lastName);
        } else {
            result = students.getAll();
            LOG.info("REST: Liste mit {} Studierenden geliefert.", result.size());
        }
        return result;
    }

    /**
     * Liest einen Studierenden anhand seiner ID.
     *
     * @param id ID eines Studierenden.
     * @return Studierender (StudentDto).
     */
    @Tag(name = "Students")
    @Get("/{id}")
    public StudentDto get(final long id) {
        final StudentDto student = students.getById(id);
        LOG.info("REST: Studierender {} {}.", student, student != null ? "geliefert" : "nicht gefunden");
        return student;
    }

    /**
     * Erstellt einen neuen Studierenden.
     *
     * @param student Studierender (StudentDto).
     * @return Studierender (StudentDto).
     */
    @Tag(name = "Students")
    @Post(value = "/", consumes = MediaType.APPLICATION_JSON)
    public StudentDto create(@Body final StudentDto student) {
        final StudentDto created = students.create(student);
        LOG.info("REST: Studierender {} gespeichert.", created);
        return created;
    }

    /**
     * Verändert einen bestehenden Studierenden.
     *
     * @param id      ID des Studierenden.
     * @param student Studierender (StudentDto).
     * @return Studierender (StudentDto).
     */
    @Tag(name = "Students")
    @Put(value = "/{id}", consumes = MediaType.APPLICATION_JSON)
    public StudentDto update(@NonNull final long id, @Body final StudentDto student) {
        student.setId(id);
        StudentDto updated = students.update(student);
        LOG.info("REST: Studierender {} aktualisiert/erzeugt.", updated);
        return updated;
    }

    /**
     * Löscht einen Studierenden anhand seiner ID.
     *
     * @param id ID eines Studierenden.
     */
    @Tag(name = "Students")
    @Delete("/{id}")
    public void delete(@NonNull final int id) {
        final boolean deleted = students.delete(id);
        LOG.info("REST: Studierender mit ID={} {} geloescht.", id, deleted ? "" : " nicht ");
    }
}
