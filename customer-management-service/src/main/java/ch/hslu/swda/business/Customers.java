package ch.hslu.swda.business;

import ch.hslu.swda.entities.Customer;

import java.util.List;

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
    Customer getById(int id);

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
     * Removes a customer from the storage.
     * @param id
     * @return true on success, false on failure
     */
    boolean deleteCustomer(int id);

    /**
     * Updates the first name of a customer.
     * @param id
     * @param firstname
     * @return true on success, false on failure.
     */
    boolean updateFirstname(int id, String firstname);

    /**
     * Updates the first name of a customer.
     * @param id
     * @param lastname
     * @return true on success, false on failure.
     */
    boolean updateLastname(int id, String lastname);

    /**
     * Updates the first and last name of a customer.
     * @param id
     * @param firstname
     * @param lastname
     * @return true on success, false on failure.
     */
    boolean updateCustomer(int id, String firstname, String lastname);
}
