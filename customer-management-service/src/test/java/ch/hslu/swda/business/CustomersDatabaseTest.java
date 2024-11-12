package ch.hslu.swda.business;

import ch.hslu.swda.entities.Customer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.UuidRepresentation;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.*;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomersDatabaseTest {

    @Container
    private static GenericContainer<?> mongoContainer = new GenericContainer<>(DockerImageName.parse("mongo:5.0"))
            .withExposedPorts(27017);

    private static CustomersDatabase customers;
    private static Map<UUID, Customer> customerMap = new HashMap<>();
    private static List<UUID> uuidList = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        mongoContainer.start();
        String host = mongoContainer.getHost();
        Integer port = mongoContainer.getMappedPort(27017);
        String mongoUri = String.format("mongodb://%s:%d", host, port);
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        customers = new CustomersDatabase(settings);
    }

    @AfterAll
    public static void tearDown() {
        mongoContainer.stop();
    }

    @Test
    @Order(1)
    void testAddCustomer() {
        Customer customer1 = new Customer(UUID.randomUUID(), "John", "Doe");
        customers.addCustomer(customer1);
        customerMap.put(customer1.getId(), customer1);
        uuidList.add(customer1.getId());
        Customer customer2 = new Customer(UUID.randomUUID(), "Dwayne", "Johnson");
        customers.addCustomer(customer2);
        customerMap.put(customer2.getId(), customer2);
        uuidList.add(customer2.getId());
        Customer customer3 = new Customer(UUID.randomUUID(), "Mary", "Smith");
        customers.addCustomer(customer3);
        customerMap.put(customer3.getId(), customer3);
        uuidList.add(customer3.getId());
        Customer customer4 = new Customer(UUID.randomUUID(), "Paul", "Johnson");
        customers.addCustomer(customer4);
        customerMap.put(customer4.getId(), customer4);
        uuidList.add(customer4.getId());
        assertEquals(4, customers.getAll().size());
    }

    @Test
    @Order(2)
    void testGetById() {
        UUID uuid = uuidList.get(1);
        Customer expectedCustomer = customerMap.get(uuid);
        Customer customer = customers.getById(uuid);
        assertEquals(expectedCustomer, customer);
    }

    @Test
    @Order(2)
    void testGetAll() {
        List<Customer> list = customers.getAll();
        System.out.println(list);
        System.out.println(customerMap);
        assertEquals(4, list.size());
    }

    @Test
    @Order(2)
    void testFindByFirstname() {
        Customer expectedCustomer = customerMap.get(uuidList.get(2));
        List<Customer> list = customers.findByFirstname("Mary");
        Customer customer = list.get(0);
        assertEquals(expectedCustomer, customer);
    }

    @Test
    @Order(2)
    void testFindByLastname() {
        Customer expectedCustomer1 = customerMap.get(uuidList.get(1));
        Customer expectedCustomer2 = customerMap.get(uuidList.get(3));
        List<Customer> list = customers.findByLastname("Johnson");
        System.out.println(list);
        System.out.println(expectedCustomer1);
        System.out.println(expectedCustomer2);
        assertEquals(2, list.size());
        assertTrue(list.contains(expectedCustomer1));
        assertTrue(list.contains(expectedCustomer2));
    }

    @Test
    @Order(4)
    void testDeleteCustomer() {
        UUID uuid = uuidList.get(0);
        customers.deleteCustomer(uuid);
        Customer customer = customers.getById(uuid);
        assertNull(customer);
    }

    @Test
    @Order(3)
    void testUpdateFirstname() {
        UUID uuid = uuidList.get(3);
        customers.updateFirstname(uuid, "Pauline");
        Customer customer = customers.getById(uuid);
        assertEquals("Pauline", customer.getFirstname());
    }

    @Test
    @Order(3)
    void testUpdateLastname() {
        UUID uuid = uuidList.get(3);
        customers.updateLastname(uuid, "Smith");
        Customer customer = customers.getById(uuid);
        assertEquals("Smith", customer.getLastname());
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        UUID uuid = uuidList.get(0);
        customers.updateCustomer(uuid, "Jonathon", "Black");
        Customer customer = customers.getById(uuid);
        assertEquals("Jonathon", customer.getFirstname());
        assertEquals("Black", customer.getLastname());
    }
}