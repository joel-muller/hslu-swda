/*
 * Copyright 2024 Roland Gisler, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.swda.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Einfaches Datenmodell für eine Addresse.
 */
@Entity
@Table(name = "address")
public final class Address implements Serializable {

    private static final long serialVersionUID = 3027754649603804325L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street = "";
    private String city = "";

    /**
     * Default Konstruktor.
     */
    public Address() {
        super();
    }

    /**
     * Konstruktor.
     *
     * @param street Strassename.
     * @param city Ortname
     */
    public Address(@NotNull final String street, @NotNull final String city) {
        this.street = street;
        this.city = city;
    }

    /**
     * Getter fuer ID.
     *
     * @return ID.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter fuer Strasse.
     *
     * @return Strassenname.
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Getter fuer Ort.
     *
     * @return Ortsname.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Setter für ID.
     *
     * @param id Neue ID.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Setter fuer Strasse.
     *
     * @param street Strasse.
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * Setter fuer city.
     *
     * @param city Setzt city.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        return object instanceof Address other
                && Objects.equals(this.id, other.id);
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Address[ID:" + this.id + ", " + this.street + ", " + this.city + "]";
    }
}
