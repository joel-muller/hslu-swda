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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Einfaches Modell einer Customer. Hat eine n:1 Beziehung zur Address.
 */
@Entity
@Table(name = "student")
public final class Student implements Serializable {

    private static final long serialVersionUID = -8844283174296254891L;

    /**
     * Primarykey als long.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nachname.
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name = "";

    /**
     * Vorname.
     */
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname = "";

    /**
     * Adresse.
     */
    @ManyToOne()
    private Address address;

    /**
     * Default Konstruktor.
     */
    public Student() {
        super();
    }

    /**
     * Konstruktor fuer Customer.
     *
     * @param name Name.
     * @param firstname Vorname.
     * @param address Adress-Objekt.
     */
    public Student(@NotNull final String name, @NotNull final String firstname, final Address address) {
        this.name = name;
        this.firstname = firstname;
        this.address = address;
    }

    /**
     * Getter fuer id.
     *
     * @return Liefert id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter fuer Name.
     *
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter fuer Vorname.
     *
     * @return Vorname.
     */
    public String getFirstname() {
        return firstname;
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
     * Setter fuer name.
     *
     * @param name Setzt name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Setter fuer firstname.
     *
     * @param firstname Setzt firstname.
     */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    /**
     * Getter fuer address.
     *
     * @return Liefert address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Setter fuer address.
     *
     * @param address Setzt address.
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        return object instanceof Student other
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
        return "Customer[ID:" + this.id + ", " + this.name + ", " + this.firstname + ", " + this.address + "]";
    }
}
