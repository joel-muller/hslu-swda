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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Testfälle für {@link ch.hslu.swda.micro.model.Address}.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class AddressTest {

    /**
     * Test für {@link ch.hslu.swda.micro.model.Address#getStreet()}.
     */
    @Test
    public void testGetStreet() {
        assertEquals("Strasse", new Address("Strasse", "Ortschaft").getStreet());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Address#setStreet(String)}.
     */
    @Test
    public void testSetStreet() {
        final Address adresse = new Address("leer", "leer");
        adresse.setStreet("Strasse");
        assertEquals("Strasse", adresse.getStreet());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Address#getCity()}.
     */
    @Test
    public void testGetCity() {
        assertEquals("Ortschaft", new Address("Strasse", "Ortschaft").getCity());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Address#setCity(String)}.
     */
    @Test
    public void testSetCity() {
        final Address adresse = new Address("leer", "leer");
        adresse.setCity("Ortschaft");
        assertEquals("Ortschaft", adresse.getCity());
    }

    /**
     * Test für {@link ch.hslu.swda.micro.model.Address#toString()}.
     */
    @Test
    public void testToString() {
        assertThat(new Address("Strasse", "Ortschaft").toString()).contains("Strasse").contains("Ortschaft");
    }

    /**
     * Test Equals-Contract.
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Address.class).suppress(Warning.SURROGATE_KEY).verify();
    }
}
