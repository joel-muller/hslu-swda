package ch.hslu.swda.entities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class StoreManagementDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/store_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(StoreManagementDB.class);
    private Connection connection;

    public StoreManagementDB() {
        try {
            connectToDatabase();
            createInventoryTable();
        } catch (SQLException e) {
            LOG.error("Error initializing StoreDB", e);
        }
    }

    private void connectToDatabase() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOG.info("Connected to the MySQL database successfully.");
        } catch (SQLException e) {
            LOG.error("Failed to connect to the MySQL database.", e);
            throw e;
        }
    }

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

    public void insertInventoryRecord(int store, int articleID, int minimumQuantity, int actualQuantity)
            throws SQLException {
        String sql = "INSERT INTO inventory (store, article_id, minimum_quantity, actual_quantity) VALUES (?, ?, ?, ?)";
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
}
