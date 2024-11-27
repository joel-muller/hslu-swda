package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.State;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesOutgoing.OrderReady;
import ch.hslu.swda.messagesIngoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UpdateOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateOrder.class);

    @Override
    public void modify(Order order, IngoingMessage responseRaw, Service service) {
        OrderUpdate update = (OrderUpdate) responseRaw;
        LOG.info("Order update did go in to the order service {}", update.toString());
        try {
            if (!update.valid()) {
                order.getState().setCancelled(true);
                return;
            }
            List<Integer> readyOrders = update.articles();
            for (int article : readyOrders) {
                order.setArticleInStore(article);
            }
            if (order.allArticlesDelivered()) {
                State state = order.getState();
                state.setArticlesReady(true);

                //remove following line only for testing
                state.setCustomerReady(true);
            }
            if (order.getState().isReady()) {
                service.sendOrderReadyToStore(new OrderReady(order.getId(), order.getStoreId()));
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to update the order {}", e.getMessage());
        }
    }
}
