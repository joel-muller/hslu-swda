package ch.hslu.swda.micro;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;
import ch.hslu.swda.entities.StoreManagementDB;

/**
 * The InventoryUpdateReceiver class implements the MessageReceiver interface
 * and is responsible for receiving
 * inventory update messages from a specified exchange, processing the messages,
 * and updating the store management
 * database accordingly.
 * 
 * <p>
 * This class uses a BusConnector for communication and a StoreManagementDB for
 * database operations. It logs
 * received messages and updates the inventory records in the database.
 * </p>
 * 
 * <p>
 * Upon receiving a message, it parses the message to extract inventory details
 * such as store, article ID,
 * minimum quantity, and actual quantity. It then updates the inventory record
 * in the database and sends a reply
 * message indicating the successful update.
 * </p>
 * 
 * The content of the received messages is expected to be the following:
 * 
 * store: int
 * articleId : int
 * minimuimQuantity: int
 * actualQuantity: int
 * 
 * 
 * <p>
 * Exceptions such as IOException and SQLException are caught and logged.
 * </p>
 * 
 * @see MessageReceiver
 * @see BusConnector
 * @see StoreManagementDB
 */
public class InventoryUpdateReceiver implements MessageReceiver {
	private static final Logger LOG = LoggerFactory.getLogger(InventoryUpdateReceiver.class);
	private final String exchangeName;
	private final BusConnector bus;
	private StoreManagementDB db;

	/**
	 * Constructs an InventoryUpdateReceiver with the specified exchange name, bus
	 * connector, and store management database.
	 *
	 * @param exchangeName the name of the exchange to listen for inventory updates
	 * @param bus          the bus connector for communication
	 * @param db           the store management database
	 */
	public InventoryUpdateReceiver(String exchangeName, BusConnector bus, StoreManagementDB db) {
		this.exchangeName = exchangeName;
		this.bus = bus;
		this.db = db;
	}

	@Override
	public void onMessageReceived(String route, String replyTo, String corrId, String message) {
		try {
			LOG.debug("received chat message with replyTo property [{}] and Message: [{}]", replyTo, message);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode inventoryUpdateNode = mapper.readTree(message);
			String storeString = inventoryUpdateNode.get("store").toString();
			String articleIdString = inventoryUpdateNode.get("articleId").toString();
			String minimumQuantityString = inventoryUpdateNode.get("minimumQuantity").toString();
			String actualQuantityString = inventoryUpdateNode.get("actualQuantity").toString();
			int store = Integer.parseInt(storeString);
			int articleId = Integer.parseInt(articleIdString);
			int minimumQuantity = Integer.parseInt(minimumQuantityString);
			int actualQuantity = Integer.parseInt(actualQuantityString);

			this.db.insertInventoryRecord(store, articleId, minimumQuantity, actualQuantity);
			LOG.info("Inventory updated. Store: {}, Article ID: {}, Minimum Quantity: {}, Actual Quantity: {}", store,
					articleId, minimumQuantity, actualQuantity);

			LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
			bus.reply(this.exchangeName, replyTo, corrId, "Inventory updated. Store: " + store + ", Article ID: "
					+ articleId + ", Minimum Quantity: " + minimumQuantity + ", Actual Quantity: " + actualQuantity);

		} catch (IOException | SQLException e) {

		}
	}

}
