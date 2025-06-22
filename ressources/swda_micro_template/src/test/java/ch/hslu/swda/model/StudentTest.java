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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * Testfälle für {@link ch.hslu.swda.micro.model.Student}.
 */
final class StudentTest {

    /**
     * Test method for
     * {@link ch.hslu.swda.micro.model.Student#Customer(long, String, String, Address)} .
     */
    @Test
    public void testPersonStringString() {
        final Student person = new Student("Gisler", "Roland", new Address("Strasse", "Ort"));
        assertThat(person.getName()).contains("Gisler");
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#getId()}.
     */
    @Test
    public void testGetId() {
        assertNull(new Student("Name", "Vorname", new Address("Strasse", "Ort")).getId());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals("Name", new Student("Name", "Vorname", new Address("Strasse", "Ort")).getName());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#setName(String)}.
     */
    @Test
    public void testSetName() {
        final Student person = new Student("leer", "leer", new Address("leer", "leer"));
        person.setName("Name");
        assertEquals("Name", person.getName());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#getFirstname()}.
     */
    @Test
    public void testGetFirstname() {
        assertEquals("Vorname", new Student("Name", "Vorname", new Address("Strasse", "Ort")).getFirstname());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#setFirstname(String)}.
     */
    @Test
    public void testSetFirstname() {
        final Student person = new Student("leer", "leer", new Address("leer", "leer"));
        person.setFirstname("Vorname");
        assertEquals("Vorname", person.getFirstname());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#getAddress()}.
     */
    @Test
    public void testGetAdresse() {
        final Address adresse = new Address("Strasse", "Ort");
        assertEquals(adresse, new Student("Name", "Vorname", adresse).getAddress());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#setAddress(Address)}.
     */
    @Test
    public void testSetAddress() {
        final Address adresse = new Address("Strasse", "Ort");
        final Student person = new Student("leer", "leer", new Address("leer", "leer"));
        person.setAddress(adresse);
        assertEquals(adresse, person.getAddress());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Student#toString()}.
     */
    @Test
    public void testToString() {
        assertNotNull(new Student("Name", "Vorname", new Address("Strasse", "Ort")).toString());
    }

    /**
     * Test Equals-Contrakt.
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Student.class).suppress(Warning.SURROGATE_KEY).verify();
    }
}
