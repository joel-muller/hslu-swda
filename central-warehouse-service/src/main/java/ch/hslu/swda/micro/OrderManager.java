package ch.hslu.swda.micro;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.persistence.CentralWarehouseOrderPersistor;
import ch.hslu.swda.stock.api.Stock;
import ch.hslu.swda.stock.api.StockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
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
        try {
            persistor.save(order);
        }catch (IOException e){
            LOG.error("Saving order failed. "+e.getMessage());
        }
        updateOrders();

    }

    public void updateOrders(){
        List<CentralWarehouseOrder> openOrders;
        try{
         openOrders = persistor.getAllOpen();
        }catch (IOException e){
            LOG.error("getting open Orders failed."+ e.getMessage());
            return;
        }
        if (openOrders==null) return;
        for (CentralWarehouseOrder currOrder : openOrders) {
            List<OrderArticle> articles = currOrder.getArticles();
            for(OrderArticle currArticle : articles){
                if(currArticle.getFulfilled()==currArticle.getCount())
                    continue;
                updateArticle(currArticle);
            }
            try {
                persistor.save(currOrder);
            }catch (IOException e){
                LOG.error("Saving order failed. "+e.getMessage());
            }
        }
    }

    private void updateArticle(OrderArticle article){
        int id = article.getId();
        int fulfilled = article.getFulfilled();
        int count = article.getCount();

        int articlesOutstanding = count- fulfilled;
        int itemCount = stock.getItemCount(id);

        if (itemCount ==-1){
            LOG.error("Article unknown. ArticleId: "+id);
            return;
        }
        if(itemCount==0) {
            LOG.debug("No article with id: "+id + " in warehouse stock");
            LocalDate nextDeliveryDate;
            try {
                nextDeliveryDate = stock.getItemDeliveryDate(id);
            }catch (StockException e){
                LOG.error(e.getMessage());
                return;
            }
            article.setNextDeliveryDate(nextDeliveryDate);
            return;
        }
        int itemsToOrder = Math.min(itemCount, articlesOutstanding);
        int itemsOrdered = stock.orderItem(id,itemsToOrder);

        if (itemsOrdered == -1){
            LOG.error("Article unknown. ArticleId: "+id);
            return;
        }
        if(itemsOrdered != itemsToOrder) {
            LOG.info("Could not order "+itemsToOrder+" of article "+id+". Actually ordered "+ itemsOrdered);
        }

        article.setFulfilled(article.getFulfilled()+Math.min(articlesOutstanding,itemsOrdered));
    }





}
