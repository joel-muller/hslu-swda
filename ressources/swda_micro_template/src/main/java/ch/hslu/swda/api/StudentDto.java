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
package ch.hslu.swda.api;

import io.micronaut.serde.annotation.Serdeable;
import java.util.Objects;

/**
 * Einfaches Datenmodell eines Studenten.
 */
@Serdeable
public final class StudentDto {

    private static final long NOID = -1;

    private long id;
    private String firstName;
    private String lastName;
    private String ort;

    /**
     * Default Konstruktor.
     */
    public StudentDto() {
        this(NOID, "unknown", "unknown", "unknown");
    }

    /**
     * Konstruktor.
     *
     * @param id Eindeutig id.
     * @param firstName Vorname.
     * @param lastName Nachname.
     * @param ort Wohnort.
     */
    public StudentDto(final long id, final String firstName, final String lastName, final String ort) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ort = ort;
    }

    /**
     * @return the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set.
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Ort.
     */
    public String getOrt() {
        return this.ort;
    }

    /**
     * @param ort Wohnort.
     */
    public void setOrt(final String ort) {
        this.ort = ort;
    }

    /**
     * Studenten mit identischer ID sind gleich. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof StudentDto other
                && this.id == other.id;
    }

    /**
     * Liefert Hashcode auf Basis der ID. {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     * Liefert eine String-Repräsentation ders Studenten. {@inheritDoc}.
     */
    @Override
    public String toString() {
        return "StudentDto[id=" + this.id + ", firstName='" + this.firstName + "', lastname='" + this.lastName
                + "', ort='" + this.ort + "']";
    }
}
