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
package ch.hslu.swda.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.swda.api.StudentDto;
import ch.hslu.swda.api.Students;
import io.micronaut.context.annotation.Primary;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;

/**
 * Fake Implementation für Verwaltung von Studenten. Speichert die Daten nur in
 * einer Map (in Memory).
 */
@Primary
@Singleton
public class StudentsRepositoryMemory implements Students {

    private static final Logger LOG = LoggerFactory.getLogger(StudentsRepositoryMemory.class);
    private long idInc = 0;

    private Map<Long, StudentDto> students;

    /**
     * Erzeugt initial eine Liste mit drei Studenten.
     */
    public StudentsRepositoryMemory() {
        if (this.students == null) {
            this.students = new HashMap<>();
            this.students.put(1L, new StudentDto(1, "Herbert", "Grone", "Luzern"));
            this.students.put(2L, new StudentDto(2, "Babette", "Zweifel", "Zürich"));
            this.students.put(3L, new StudentDto(3, "Markus", "Kaiser", "Rotkreuz"));
            this.idInc = this.students.size();
            LOG.debug("API: Es wurden {} Studenten wurden erzeugt.", idInc);
        }
    }

    /**
     * @see ch.hslu.swda.api.Students#getById(long)
     */
    @Override
    @NewSpan("students.getByID()")
    public StudentDto getById(@SpanTag("studentId") final long id) {
        LOG.debug("API: Student mit id={} gelesen.", id);
        return this.students.get(id);
    }

    /**
     * @see ch.hslu.swda.api.Students#findByLastName(java.lang.String)
     */
    @Override
    @NewSpan("students.findByLastName()")
    public List<StudentDto> findByLastName(@SpanTag("studentName") final String lastName) {
        List<StudentDto> list = this.students.values().stream().filter(s -> s.getLastName().contains(lastName))
                .collect(Collectors.toList());
        LOG.debug("API: Liste mit {} Studierenden mit Nachname '{}' gefunden.", list.size(), lastName);
        return list;
    }

    /**
     * @see ch.hslu.swda.api.Students#getAll()
     */
    @Override
    @NewSpan("students.getAll()")
    public List<StudentDto> getAll() {
        List<StudentDto> list = this.students.values().stream().collect(Collectors.toList());
        LOG.debug("API: Liste mit {} Studierenden gelesen.", students.size());
        return list;
    }

    /**
     * @see ch.hslu.swda.api.Students#create(StudentDto)
     */
    @Override
    public StudentDto create(final StudentDto student) {
        this.idInc++;
        student.setId(this.idInc);
        this.students.put(student.getId(), student);
        LOG.debug("API: Studierender '{}' erstellt.", student);
        return student;
    }

    /**
     * @see ch.hslu.swda.api.Students#update(long, StudentDto)
     */
    @Override
    public StudentDto update(final StudentDto student) {
        StudentDto exists = this.students.get(student.getId());
        if (Objects.nonNull(exists)) {
            exists.setLastName(student.getLastName());
            exists.setFirstName(student.getFirstName());
            exists.setOrt(student.getOrt());
            LOG.debug("API: Studierender {} aktualisiert.", student);
        } else {
            exists = this.create(student);
        }
        return exists;
    }

    /**
     * @see ch.hslu.swda.api.Students#delete(long)
     */
    @Override
    public boolean delete(final long id) {
        final StudentDto student = students.remove(id);
        LOG.debug("API: Studierender mit id={} geloescht.", student);
        return student != null;
    }
}
