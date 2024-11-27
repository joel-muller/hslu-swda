package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.State;
import ch.hslu.swda.messages.OrderReady;
import ch.hslu.swda.messages.OrderUpdate;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UpdateOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateOrder.class);
    private final OrderUpdate update;
    private final Service service;

    public UpdateOrder(OrderUpdate update, Service service) {
        this.update = update;
        this.service = service;
    }

    @Override
    public void modify(Order order) {
        try {
            if (!update.valid()) {
                order.getState().setCancelled(true);
                return;
            }
            List<Integer> readyOrders = this.update.articles();
            for (int article : readyOrders) {
                order.setArticleInStore(article);
            }
            if (order.allArticlesDelivered()) {
                State state = order.getState();
                state.setArticlesReady(true);
            }
            if (order.getState().isReady()) {
                service.sendOrderReadyToStore(new OrderReady(order.getId(), order.getStoreId()));
            }
        } catch (IOException e) {
            LOG.error("An error occurred while trying to update the order {}", e.getMessage());
        }
    }
}
