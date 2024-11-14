package ch.hslu.swda.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hslu.swda.micro.StoreManagementService;

import static org.junit.jupiter.api.Assertions.*;

public class StoreManagementDBTest {
    private static final Logger LOG = LoggerFactory.getLogger(StoreManagementDBTest.class);
    private StoreManagementDB testStoreManagementDB;

    @BeforeEach
    public void setUp() throws SQLException {
        String url = "jdbc:mysql://localhost:3307/store_management_test";
        String user = "root";
        String password = "root";
        this.testStoreManagementDB = new StoreManagementDB(url, user, password);
        testStoreManagementDB.connectToDatabase(url, user, password);
        //LOG.debug(testStoreManagementDB.getConnection().toString());

    }

    @Disabled
    @Test
    void testCheckArticleAvailabilityAsJson() throws SQLException {
        // TODO:
        testStoreManagementDB.checkArticleAvailabilityAsJson(123);
        // TODO: Implement test
    }

    @Test
    void testConnectToDatabase() throws SQLException {
        assertNotNull(testStoreManagementDB.getConnection());
    }



    @Test
    void testInsertInventoryRecord() {
        // testStoreManagementDB.connection = DriverManager.getConnection(DB_URL,
        // DB_USER, DB_PASSWORD);
        int storeId = 5;
        int articleId = 15;
        int minQuantity = 10;
        int actualQuantity = 30;
        try {
            testStoreManagementDB.insertInventoryRecord(5, 15, 10, 30);
            String query = "SELECT * FROM inventory WHERE article_id = ? AND store_id = ?";
            PreparedStatement preparedStatement = testStoreManagementDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, storeId);
            preparedStatement.setInt(2, articleId);

            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if record exists
            assertTrue(resultSet.next(), "Record not found in database");

            // Verify each field
            assertEquals(minQuantity, resultSet.getInt("minimum_quantity"));
            assertEquals(actualQuantity, resultSet.getInt("actual_quantity"));
        } catch (SQLException e) {
            LOG.error("Error inserting inventory record", e);
        }
    }

    @Disabled
    @Test
    void testViewInventoryTable() {
        assertDoesNotThrow(() -> testStoreManagementDB.viewInventoryTable(), "The viewInventoryTable method should not throw an exception.");
    }



}
