package ch.hslu.swda.entities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StoreManagementDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/store_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(StoreManagementDB.class);
    private Connection connection;


        /**
     * @param connection
     */
    public StoreManagementDB(String DB_URL, String DB_USER, String DB_PASSWORD) {
        try {
            //connectToDatabase(DB_URL, DB_USER, DB_PASSWORD);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            //createInventoryTable();
            LOG.info("StoreManagementDB initialized successfully.");
        } catch (SQLException e) {
            LOG.error("Error initializing StoreManagementDB", e);
        }
    }

    /**
     * @throws SQLException
     */
    void connectToDatabase(String DB_URL, String DB_USER, String DB_PASSWORD) throws SQLException {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOG.info("Connected to the StoreManagementDB MySQL database successfully.");
        } catch (SQLException e) {
            LOG.error("Failed to connect to the StoreManagementDB MySQL database.", e);
            throw e;
        }
    }

        /**
     * @throws SQLException
     */
/*     void connectToDatabase() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOG.info("Connected to the StoreManagementDB MySQL database successfully.");
        } catch (SQLException e) {
            LOG.error("Failed to connect to the StoreManagementDB MySQL database.", e);
            throw e;
        }
    } */

    /**
     * Creates the inventory table in the database if it does not already exist.
     * The table includes the following columns:
     * - store: the store identifier (VARCHAR(255), not null)
     * - article_id: the article identifier (VARCHAR(255), not null)
     * - minimum_quantity: the minimum quantity of the article (INT, not null)
     * - actual_quantity: the actual quantity of the article (INT, not null)
     * The primary key is a composite key consisting of store and article_id.
     *
     * @throws SQLException if a database access error occurs or the SQL statement is incorrect
     */
    private void createInventoryTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS inventory ("
                + "store VARCHAR(255) NOT NULL, "
                + "article_id VARCHAR(255) NOT NULL, "
                + "minimum_quantity INT NOT NULL, "
                + "actual_quantity INT NOT NULL, "
                + "PRIMARY KEY (store, article_id))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    /**
     * Inserts a record into the inventory table. If a record with the same store and article_id already exists,
     * it updates the existing record with the new values.
     *
     * @param store the store identifier
     * @param articleID the article identifier
     * @param minimumQuantity the minimum quantity of the article
     * @param actualQuantity the actual quantity of the article
     * @throws SQLException if a database access error occurs or the SQL statement is invalid
     */
    public void insertInventoryRecord(int store, int articleID, int minimumQuantity, int actualQuantity)
            throws SQLException {
        String sql = "INSERT INTO inventory (store, article_id, minimum_quantity, actual_quantity) VALUES (?, ?, ?, ?)" + "ON DUPLICATE KEY UPDATE minimum_quantity = VALUES(minimum_quantity), actual_quantity = VALUES(actual_quantity)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, store);
            preparedStatement.setInt(2, articleID);
            preparedStatement.setInt(3, minimumQuantity);
            preparedStatement.setInt(4, actualQuantity);
            preparedStatement.executeUpdate();
            LOG.info("Inserted record into inventory table successfully.");
        } catch (SQLException e) {
            LOG.error("Failed to insert record into inventory table.", e);
            throw e;
        }
    }

    /**
     * Method to view all records in the inventory table.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void viewInventoryTable() throws SQLException {
        String sql = "SELECT * FROM inventory";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String store = resultSet.getString("store");
                String articleId = resultSet.getString("article_id");
                int minimumQuantity = resultSet.getInt("minimum_quantity");
                int actualQuantity = resultSet.getInt("actual_quantity");
                LOG.info("Store: {}, Article ID: {}, Minimum Quantity: {}, Actual Quantity: {}",
                        store, articleId, minimumQuantity, actualQuantity);
                System.out.println("Store: " + store + ", Article ID: " + articleId + ", Minimum Quantity: " + minimumQuantity + ", Actual Quantity: " + actualQuantity);
            }
        } catch (SQLException e) {
            LOG.error("Failed to retrieve records from inventory table.", e);
            throw e;
        }
    }
    public ArrayNode viewInventoryTableAsJson() throws SQLException {
        String sql = "SELECT * FROM inventory";
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.createArrayNode();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                ObjectNode jsonObject = objectMapper.createObjectNode();
                jsonObject.put("store", resultSet.getString("store"));
                jsonObject.put("article_id", resultSet.getString("article_id"));
                jsonObject.put("minimum_quantity", resultSet.getInt("minimum_quantity"));
                jsonObject.put("actual_quantity", resultSet.getInt("actual_quantity"));
                jsonArray.add(jsonObject);
            }
        } catch (SQLException e) {
            LOG.error("Failed to retrieve records from inventory table.", e);
            throw e;
        }

        return jsonArray;
    }

public ArrayNode checkArticleAvailabilityAsJson(Integer articleID) throws SQLException {
    String sql = "SELECT * FROM inventory WHERE article_id = ?";
    ObjectMapper objectMapper = new ObjectMapper(); // Create ObjectMapper instance
    ArrayNode jsonArray = objectMapper.createArrayNode(); // Create ArrayNode to hold JSON objects

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, articleID);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ObjectNode jsonObject = objectMapper.createObjectNode(); // Create ObjectNode for each row
                jsonObject.put("store", resultSet.getString("store"));
                jsonObject.put("minimum_quantity", resultSet.getInt("minimum_quantity"));
                jsonObject.put("actual_quantity", resultSet.getInt("actual_quantity"));
                
                jsonArray.add(jsonObject); // Add ObjectNode to ArrayNode
            }
        }
    } catch (SQLException e) {
        LOG.error("Failed to check article availability.", e);
        throw e; // Re-throw the exception after logging
    }
    
    return jsonArray; // Return ArrayNode containing the JSON data
}

public void close() {
        try {
            if (connection != null && !connection.isClosed()){
            connection.close();}
            LOG.info("Closed connection to the StoreManagementDB MySQL database successfully.");
        } catch (SQLException e) {
            LOG.error("Failed to close connection to the StoreManagementDB MySQL database.", e);
        }
    }

public static String getDbUrl() {
    return DB_URL;
}

public static String getDbUser() {
    return DB_USER;
}

public static String getDbPassword() {
    return DB_PASSWORD;
}

public static org.slf4j.Logger getLog() {
    return LOG;
}

public Connection getConnection() {
    return connection;
}

public void setConnection(Connection connection) {
    this.connection = connection;
}

}
