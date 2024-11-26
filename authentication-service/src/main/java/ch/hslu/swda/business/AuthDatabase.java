package ch.hslu.swda.business;

import ch.hslu.swda.entities.SystemRights;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.entities.UserRole;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class AuthDatabase implements AuthStorage {

    private static final Logger LOG = LoggerFactory.getLogger(AuthDatabase.class);

    private final Datastore datastore;

    public AuthDatabase() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        datastore = Morphia.createDatastore(MongoClients.create(settings), "auth_db");
        datastore.getMapper().map(SystemRights.class);
        datastore.ensureIndexes();
        if (getAllRights().size() == 0 && getAllRoles().size() == 0) {
            setupStorage();
        }
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public User getUserById(UUID id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return false;
    }

    @Override
    public boolean updateUsername(UUID id, String username) {
        return false;
    }

    @Override
    public boolean updatePasswordHash(UUID id, String passwordHash) {
        return false;
    }

    @Override
    public boolean updateUsersUserRole(UUID id, UserRole role) {
        return false;
    }

    @Override
    public boolean updateUser(UUID id, String username, String passwordHash, UserRole role) {
        return false;
    }

    @Override
    public SystemRights getRightsById(UUID id) {
        SystemRights rights = datastore.find(SystemRights.class)
                .filter(eq("_id", id))
                .first();
        LOG.debug("SystemRights with id [{}] retrieved", id);
        return rights;
    }

    @Override
    public SystemRights getRightsByName(String name) {
        SystemRights rights = datastore.find(SystemRights.class)
                .filter(eq("name", name))
                .first();
        LOG.debug("SystemRights with name [{}] retrieved", name);
        return rights;
    }

    @Override
    public List<SystemRights> getAllRights() {
        List<SystemRights> rightsList = datastore.find(SystemRights.class)
                .iterator(new FindOptions())
                .toList();
        LOG.debug("SystemRights list of size [{}] retrieved", rightsList.size());
        return rightsList;
    }

    @Override
    public UserRole getRoleById(UUID id) {
        UserRole role = datastore.find(UserRole.class)
                .filter(eq("_id", id))
                .first();
        LOG.debug("UserRole with id [{}] retrieved", id);
        return role;
    }

    @Override
    public UserRole getRoleByName(String name) {
        UserRole role = datastore.find(UserRole.class)
                .filter(eq("name", name))
                .first();
        LOG.debug("UserRole with id [{}] retrieved", name);
        return role;
    }

    @Override
    public List<UserRole> getAllRoles() {
        List<UserRole> rolesList = datastore.find(UserRole.class)
                .iterator(new FindOptions())
                .toList();
        LOG.debug("UserRoles list of size [{}] retrieved", rolesList.size());
        return rolesList;
    }

    /**
     * Sets up the necessary user roles and system rights for the database.
     * Initiate the class in any other instance with the interface, so that this method is not available outside of setup.
     */
    public void setupStorage() {
        LOG.info("Setting up storage...");
        List<SystemRights> rightsList = new ArrayList<>();
        Map<String, SystemRights> rightsMap = new HashMap<>();
        rightsList.add(new SystemRights("user.crud_all"));
        rightsList.add(new SystemRights("customer.crud_all"));
        rightsList.add(new SystemRights("customer.crud_nodelete"));
        rightsList.add(new SystemRights("order.crud_all"));
        rightsList.add(new SystemRights("store.warehouse"));
        rightsList.add(new SystemRights("store.status"));
        rightsList.add(new SystemRights("store.all"));
        rightsList.add(new SystemRights("logs.read"));

        for (SystemRights rights : rightsList) {
            LOG.info("Storing rights: [{}]", rights.getName());
            datastore.save(rights);
            rightsMap.put(rights.getName(), rights);
        }

        List<UserRole> rolesList = new ArrayList<>();
        rolesList.add(new UserRole("sys_admin", List.of(
                rightsMap.get("user.crud_all"),
                rightsMap.get("customer.crud_all"),
                rightsMap.get("order.crud_all"),
                rightsMap.get("store.all"),
                rightsMap.get("logs.read")
        )));
        rolesList.add(new UserRole("sales_personell", List.of(
                rightsMap.get("customer.crud_nodelete"),
                rightsMap.get("order.crud_all")
        )));
        rolesList.add(new UserRole("data_typist", List.of(
                rightsMap.get("store.warehouse")
        )));
        rolesList.add(new UserRole("store_manager", List.of(
                rightsMap.get("customer.crud_all"),
                rightsMap.get("order.crud_all"),
                rightsMap.get("store.status"),
                rightsMap.get("logs.read")
        )));

        for (UserRole role : rolesList) {
            LOG.info("Store role: [{}]", role.getName());
            datastore.save(role);
        }
    }
}
