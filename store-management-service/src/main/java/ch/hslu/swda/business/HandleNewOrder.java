package ch.hslu.swda.business;

import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.Store;
import ch.hslu.swda.entities.StoreArticle;
import ch.hslu.swda.messagesIngoing.IngoingMessage;
import ch.hslu.swda.messagesIngoing.OrderRequest;
import ch.hslu.swda.messagesOutgoing.OrderUpdate;
import ch.hslu.swda.micro.Service;
import ch.hslu.swda.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandleNewOrder implements Modifiable {
    private static final Logger LOG = LoggerFactory.getLogger(HandleNewOrder.class);

    @Override
    public void modify(DatabaseConnector databaseConnector, IngoingMessage responseRaw, Service service) {
        try {
            LOG.info("Handle new order");
            OrderRequest request = (OrderRequest) responseRaw;
            Order order = Order.createFromOrderRequest(request);
            Store store = databaseConnector.getStore(request.getStoreId());
            store.addOrder(order);
            List<OrderArticle> articlesOrdered = order.getArticleOrderedList();
            List<Integer> articleValidated = new ArrayList<>();
            for (OrderArticle article : articlesOrdered) {
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
            databaseConnector.storeStore(store);
            LOG.info("New order {} arrived for the store {}", order.getId(), store.getId());
        } catch (IOException e) {
            LOG.error("Exception occurred while trying to update the order {}", e.getMessage());
        }
//        catch (NoSuchElementException e) {
//            //TBD for when a store does not exist
//        }
    }
}
