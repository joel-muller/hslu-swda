package ch.hslu.swda.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    private State state;

    @BeforeEach
    void setUp() {
        state = new State();
    }

    @Test
    void isReady() {
        assertFalse(state.isReady(), "Initially, isReady should return false");

        state.setArticlesReady(true);
        assertFalse(state.isReady(), "isReady should return false if only articles are ready");

        state.setCustomerReady(true);
        assertTrue(state.isReady(), "isReady should return true if both articlesReady and customerReady are true");

        state.setCustomerReady(false);
        assertFalse(state.isReady(), "isReady should return false if customerReady is false");
    }

    @Test
    void isValid() {
        assertFalse(state.isValid(), "Initially, valid should be false");

        state.setValid(true);
        assertTrue(state.isValid(), "After setting valid to true, isValid should return true");
    }

    @Test
    void isArticlesReady() {
        assertFalse(state.isArticlesReady(), "Initially, articlesReady should be false");

        state.setArticlesReady(true);
        assertTrue(state.isArticlesReady(), "After setting articlesReady to true, isArticlesReady should return true");
    }

    @Test
    void isCustomerReady() {
        assertFalse(state.isCustomerReady(), "Initially, customerReady should be false");

        state.setCustomerReady(true);
        assertTrue(state.isCustomerReady(), "After setting customerReady to true, isCustomerReady should return true");
    }

    @Test
    void isDelivered() {
        assertFalse(state.isDelivered(), "Initially, delivered should be false");

        state.setDelivered(true);
        assertTrue(state.isDelivered(), "After setting delivered to true, isDelivered should return true");
    }

    @Test
    void isCancelled() {
        assertFalse(state.isCancelled(), "Initially, cancelled should be false");

        state.setCancelled(true);
        assertTrue(state.isCancelled(), "After setting cancelled to true, isCancelled should return true");
    }

    @Test
    void setValid() {
        state.setValid(true);
        assertTrue(state.isValid(), "setValid(true) should make isValid return true");

        state.setValid(false);
        assertFalse(state.isValid(), "setValid(false) should make isValid return false");
    }

    @Test
    void setArticlesReady() {
        state.setArticlesReady(true);
        assertTrue(state.isArticlesReady(), "setArticlesReady(true) should make isArticlesReady return true");

        state.setArticlesReady(false);
        assertFalse(state.isArticlesReady(), "setArticlesReady(false) should make isArticlesReady return false");
    }

    @Test
    void setCustomerReady() {
        state.setCustomerReady(true);
        assertTrue(state.isCustomerReady(), "setCustomerReady(true) should make isCustomerReady return true");

        state.setCustomerReady(false);
        assertFalse(state.isCustomerReady(), "setCustomerReady(false) should make isCustomerReady return false");
    }

    @Test
    void setDelivered() {
        state.setDelivered(true);
        assertTrue(state.isDelivered(), "setDelivered(true) should make isDelivered return true");

        state.setDelivered(false);
        assertFalse(state.isDelivered(), "setDelivered(false) should make isDelivered return false");
    }

    @Test
    void setCancelled() {
        state.setCancelled(true);
        assertTrue(state.isCancelled(), "setCancelled(true) should make isCancelled return true");

        state.setCancelled(false);
        assertFalse(state.isCancelled(), "setCancelled(false) should make isCancelled return false");
    }

    @Test
    void testEquals() {
        State otherState = new State();
        assertEquals(state, otherState, "Two default State instances should be equal");

        state.setValid(true);
        assertNotEquals(state, otherState, "Two instances should not be equal if one has different values");

        otherState.setValid(true);
        assertEquals(state, otherState, "Two instances should be equal if they have the same values for all fields");
    }

    @Test
    void testHashCode() {
        State otherState = new State();
        assertEquals(state.hashCode(), otherState.hashCode(), "Two default State instances should have the same hash code");

        state.setValid(true);
        assertNotEquals(state.hashCode(), otherState.hashCode(), "Hash codes should differ if instances have different values");

        otherState.setValid(true);
        assertEquals(state.hashCode(), otherState.hashCode(), "Hash codes should match if all fields are identical");
    }

    @Test
    void testToString() {
        String expectedString = "State{valid=false, articlesReady=false, customerReady=false, delivered=false, cancelled=false}";
        assertEquals(expectedString, state.toString(), "toString should match the expected format for default state");

        state.setValid(true);
        state.setArticlesReady(true);
        state.setCustomerReady(true);
        state.setDelivered(true);
        state.setCancelled(true);
        String updatedString = "State{valid=true, articlesReady=true, customerReady=true, delivered=true, cancelled=true}";
        assertEquals(updatedString, state.toString(), "toString should reflect the current values of all fields");
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier.simple().forClass(State.class)
                .verify();
    }

}