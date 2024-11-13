package ch.hslu.swda.micro;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.persistence.CentralWarehouseOrderPersistor;
import ch.hslu.swda.stock.api.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderManager  implements CentralWarehouseOrderManager{

    private final Stock stock;

    private final CentralWarehouseOrderPersistor persistor;
    private static final Logger LOG = LoggerFactory.getLogger(OrderManager.class);

    public OrderManager(Stock stock, CentralWarehouseOrderPersistor persistor){
        this.stock = stock;
        this.persistor = persistor;
    }


    public void process(CentralWarehouseOrder order){
        persistor.save(order);
        updateOrders();
    }

    public void updateOrders(){

        List<CentralWarehouseOrder> openOrders = persistor.getAllOpen();
        for (CentralWarehouseOrder currOrder : openOrders) {
            List<OrderArticle> articles = currOrder.getArticles();
            for(OrderArticle currArticle : articles){
                if(currArticle.getFulfilled()==currArticle.getCount())
                    continue;
                updateArticle(currArticle);
            }
            persistor.save(currOrder);
        }
    }

    private void updateArticle(OrderArticle article){
        int articlesOutstanding = article.getCount()- article.getFulfilled();
        int itemCount = stock.getItemCount(article.getId());

        if (itemCount ==-1){
            LOG.error("Article unknown. ArticleId: "+article.getId());
            return;
        }
        if(itemCount==0) {
            LOG.debug("No article with id: "+article.getId() + " in warehouse stock");
            return;
        }
        int itemsToOrder = Math.min(itemCount, articlesOutstanding);
        int itemsOrdered = stock.orderItem(article.getId(),itemsToOrder);

        if (itemsOrdered == -1){
            LOG.error("Article unknown. ArticleId: "+article.getId());
            return;
        }
        if(itemsOrdered != itemsToOrder) {
            LOG.info("Could not order "+itemsToOrder+" of article "+article.getId()+". Actually ordered "+ itemsOrdered);
        }

        article.setFulfilled(article.getFulfilled()+Math.min(articlesOutstanding,itemsOrdered));
    }





}
