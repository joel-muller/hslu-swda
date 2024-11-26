package ch.hslu.swda.business;

import ch.hslu.swda.entities.SystemRights;
import ch.hslu.swda.entities.User;
import ch.hslu.swda.entities.UserRole;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.experimental.updates.UpdateOperators;
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
        datastore.getMapper().map(SystemRights.class, UserRole.class, User.class);
        datastore.ensureIndexes();
        if (getAllRights().size() == 0 && getAllRoles().size() == 0) {
            setupStorage();
        }
    }

    @Override
    public boolean addUser(User user) {
        LOG.debug("Storing user [{}]", user);
        datastore.save(user);
        return true;
    }

    @Override
    public User getUserById(UUID id) {
        User user = datastore.find(User.class)
                .filter(eq("_id", id))
                .first();
        LOG.debug("User with id [{}] retrieved", id);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = datastore.find(User.class)
                .filter(eq("username", username))
                .first();
        LOG.debug("User with username [{}] retrieved", username);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = datastore.find(User.class)
                .iterator(new FindOptions())
                .toList();
        LOG.debug("User list of size [{}] retrieved", userList.size());
        return userList;
    }

    @Override
    public boolean deleteUser(UUID id) {
        User user = getUserById(id);
        DeleteResult result = datastore.find(User.class)
                .filter(eq("_id", id))
                .delete(new DeleteOptions());
        if (user != null) {
            LOG.info("User of id [{}] and username [{}] was deleted", id, user.getUsername());
        }
        LOG.info("User of id [{}] was attempted to be deleted", id);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateUsername(UUID id, String username) {
        UpdateResult result = datastore.find(User.class)
                .filter(eq("_id", id))
                .update(UpdateOperators.set("username", username))
                .execute();
        LOG.debug("Username of user with id [{}] was updated to [{}]", id, username);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updatePasswordHash(UUID id, String passwordHash) {
        UpdateResult result = datastore.find(User.class)
                .filter(eq("_id", id))
                .update(UpdateOperators.set("passwordHash", passwordHash))
                .execute();
        LOG.debug("Password was updated for user with id [{}]", id);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateUserRole(UUID id, UserRole role) {
        UpdateResult result = datastore.find(User.class)
                .filter(eq("_id", id))
                .update(UpdateOperators.set("role", role))
                .execute();
        LOG.debug("Role of user with id [{}] was updated to [{}]", id, role);
        return result.wasAcknowledged();
    }

    @Override
    public boolean updateUser(UUID id, String username, String passwordHash, UserRole role) {
        boolean updatedUsername = updateUsername(id, username);
        if (!updatedUsername) {
            return false;
        }
        boolean updatedPasswordHash = updatePasswordHash(id, passwordHash);
        if (!updatedPasswordHash) {
            return false;
        }
        return updateUserRole(id, role);
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
