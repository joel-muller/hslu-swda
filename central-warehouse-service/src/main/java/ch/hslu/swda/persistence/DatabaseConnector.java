
    package ch.hslu.swda.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

    public class DatabaseConnector {

        private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

        private final String jdbcUrl;
        private final String user;
        private final String password;

        public DatabaseConnector(String host, String port, String dbName, String user, String password) {
            this.jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?serverTimezone=UTC";
            this.user = user;
            this.password = password;
        }

        public Connection getConnection() {
            int retryCount = 0;
            int retryDelaySeconds =1;

            while (true) {
                retryDelaySeconds += 1;
                try {
                    LOG.debug("Attempting to connect to database: {}", jdbcUrl);
                    Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
                    LOG.info("Successfully connected to the database.");
                    return connection;
                } catch (SQLException e) {
                    retryCount++;
                    LOG.error("Connection attempt {} failed: {}. Retry in {} seconds.", retryCount, e.getMessage(),retryDelaySeconds);
                    try {
                        Thread.sleep(Duration.ofSeconds(retryDelaySeconds).toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Database connection retry interrupted.", ie);
                    }
                }
            }
        }
    }
