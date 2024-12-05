package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseWrapper {
    public static DBOrder createDBOrder(Order order) {
        List<DBArticle> dbArticleList = new ArrayList<>();
        List<Article> articleList = order.getCopyOfArticles();
        for (Article article : articleList) {
            dbArticleList.add(new DBArticle(article.getId(), article.getCount(), article.isDelivered(), article.getPrice().getFrancs(), article.getPrice().getCentimes()));
        }
        return new DBOrder(order.getId(), dbArticleList, order.getDate(), order.getStoreId(), order.getCustomerId(), order.getEmployeeId(), order.getState().toNumber(), order.isCancelled());
    }

    public static Order createOrder(DBOrder dbOrder) {
        List<DBArticle> dbArticleList = dbOrder.getArticles();
        List<Article> articleList = new ArrayList<>();
        for (DBArticle dbArticle : dbArticleList) {
            Article article = new Article(dbArticle.getId(), dbArticle.getCount(), new Price(dbArticle.getFrancs(), dbArticle.getCentimes()));
            article.setDelivered(dbArticle.isDelivered());
            articleList.add(article);
        }
        return new Order(dbOrder.getId(), dbOrder.getDate(), dbOrder.getStoreId(), dbOrder.getCustomerId(), dbOrder.getEmployeeId(), articleList, StateEnum.fromNumber(dbOrder.getState()), dbOrder.isCancelled());
    }
}
