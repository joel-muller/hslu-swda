package ch.hslu.swda.business;

import ch.hslu.swda.entities.ArticleOrdered;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messages.OrderRequest;
import ch.hslu.swda.messages.OrderUpdate;
import ch.hslu.swda.micro.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessOrderStore implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOrderStore.class);
    private final Service service;
    private final DatabaseConnector databaseConnector;
    private final OrderRequest request;

    public ProcessOrderStore(Service service, DatabaseConnector databaseConnector, OrderRequest request) {
        this.service = service;
        this.databaseConnector = databaseConnector;
        this.request = request;
    }

    @Override
    public void modify(Store store) {
        try {
            Order order = Order.createFromOrderRequest(request);
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
            databaseConnector.storeOrder(order);
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
    }
}
