/*
 * Copyright 2024, Roland Gisler, Hochschule Luzern - Informatik.
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

import java.util.List;

/**
 * Einfache Businesslogik für Verwaltung von Studierenden.
 */
public interface Students {

    /**
     * Liefert ein StudentDto.
     *
     * @param id ID des Studenten.
     * @return StudentDto.
     */
    StudentDto getById(long id);

    /**
     * Sucht nach Studierenden.
     *
     * @param lastName Nachname.
     * @return Liste von StudentDto.
     */
    List<StudentDto> findByLastName(String lastName);

    /**
     * Liefert alle Studierenden.
     *
     * @return Liste von StudentDto.
     */
    List<StudentDto> getAll();

    /**
     * Erstellt ein neues StudentDto.
     *
     * @param student StudentDto.
     * @return StudentDto.
     */
    StudentDto create(StudentDto student);

    /**
     * Aktualisiert ein StudentDto.
     *
     * @param student StudentDto.
     * @return Aktualisiertes (oder neues) StudentDto.
     */
    StudentDto update(StudentDto student);

    /**
     * Löscht ein StudentDto.
     *
     * @param id ID des StudentDto.
     * @return OK oder NOK.
     */
    boolean delete(long id);
}