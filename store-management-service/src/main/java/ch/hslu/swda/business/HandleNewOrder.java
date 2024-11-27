package ch.hslu.swda.business;

import ch.hslu.swda.entities.ArticleOrdered;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderRequest;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HandleNewOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleNewOrder.class);

    @Override
    public void modify(Store store, IngoingMessage responseRaw, Service service, DatabaseConnector database) {
        try {
            OrderRequest request = (OrderRequest) responseRaw;
            Order order = Order.createFromOrderRequest(request);
            store.addOrder(order.getId());
            List<ArticleOrdered> articlesOrdered = order.getArticleOrderedList();
            List<Integer> articleValidated = new ArrayList<>();
            for (ArticleOrdered article : articlesOrdered) {
                if (article.isReady()) {
                    continue;
                }
                StoreArticle storeArticle = store.getArticle(article.getId());
                if (storeArticle == null) {
                    // Order from central warehouse
                } else {
                    if (storeArticle.getActualQuantity() >= article.getCount()) {
                        storeArticle.decrementQuantity(article.getCount());
                        article.setReady(true);
                        articleValidated.add(article.getId());
                    } else {
                        //order from central warehouse
                    }
                }
            }
            if (!articleValidated.isEmpty()) {
                service.sendOrderUpdate(new OrderUpdate(order.getId(), articleValidated, true));
            }
            database.storeOrder(order);
            LOG.info("New order {} arrived for the store {}", order.getId(), store.getId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
