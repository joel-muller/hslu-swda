package ch.hslu.swda.business;

import ch.hslu.swda.entities.Customer;

import java.util.List;
import java.util.UUID;

/**
 * Interface for storing customers.
 */
public interface Customers {

    /**
     * Stores a customer.
     * @param customer
     * @return true on success, false on failure
     */
    boolean addCustomer(Customer customer);

    /**
     * Returns a customer.
     * @param id
     * @return Customer
     */
    Customer getById(UUID id);

    /**
     * Returns a list of customers with the specified first name.
     * @param firstname
     * @return List of customers
     */
    List<Customer> findByFirstname(String firstname);

    /**
     * Returns a list of customers with the specified last name.
     * @param lastname
     * @return List of customers
     */
    List<Customer> findByLastname(String lastname);

    /**
     * Returns a list of all customers.
     * @return List of customers
     */
    List<Customer> getAll();

    /**
     * Removes a customer from the storage.
     * @param id
     * @return true on success, false on failure
     */
    boolean deleteCustomer(UUID id);

    /**
     * Updates the first name of a customer.
     * @param id
     * @param firstname
     * @return true on success, false on failure.
     */
    boolean updateFirstname(UUID id, String firstname);

    /**
     * Updates the first name of a customer.
     * @param id
     * @param lastname
     * @return true on success, false on failure.
     */
    boolean updateLastname(UUID id, String lastname);

    /**
     * Updates the first and last name of a customer.
     * @param id
     * @param firstname
     * @param lastname
     * @return true on success, false on failure.
     */
    boolean updateCustomer(UUID id, String firstname, String lastname);
}
