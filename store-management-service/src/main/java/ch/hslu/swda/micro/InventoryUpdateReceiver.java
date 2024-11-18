package ch.hslu.swda.micro;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.swda.bus.BusConnector;
import ch.hslu.swda.bus.MessageReceiver;

public class InventoryUpdateReceiver implements MessageReceiver {
	private static final Logger LOG = LoggerFactory.getLogger(InventoryUpdateReceiver.class);
	private final String exchangeName;
	private final BusConnector bus;

	public InventoryUpdateReceiver(String exchangeName, BusConnector bus) {
		this.exchangeName = exchangeName;
		this.bus = bus;
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

			LOG.info("Inventory updated. Store: {}, Article ID: {}, Minimum Quantity: {}, Actual Quantity: {}", store,
					articleId, minimumQuantity, actualQuantity);

			LOG.debug("sending answer with topic [{}] according to replyTo-property", replyTo);
			bus.reply(this.exchangeName, replyTo, corrId, "Inventory updated. Store: " + store + ", Article ID: "
					+ articleId + ", Minimum Quantity: " + minimumQuantity + ", Actual Quantity: " + actualQuantity);

		} catch (IOException e) {
			LOG.error("An error occurred while trying to update the inventory {}", e.getMessage());
		}
	}

}
