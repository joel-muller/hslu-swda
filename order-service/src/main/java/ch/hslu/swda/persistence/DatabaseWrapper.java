package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Order;
import ch.hslu.swda.entities.State;

import java.util.ArrayList;
import java.util.List;

public class DatabaseWrapper {
    public static DBOrder createDBOrder(Order order) {
        List<DBArticle> dbArticleList = new ArrayList<>();
        List<Article> articleList = order.getArticles();
        for (Article article : articleList) {
            dbArticleList.add(new DBArticle(article.getId(), article.getCount(), article.isDelivered()));
        }
        State state = order.getState();
        DBState dbState = new DBState(state.isValid(), state.isArticlesReady(), state.isCustomerReady(), state.isDelivered(), state.isCancelled());
        return new DBOrder(order.getId(), dbState, dbArticleList, order.getDate(), order.getStoreId(), order.getCustomerId(), order.getEmployeeId());
    }

    public static Order createOrder(DBOrder dbOrder) {
        List<DBArticle> dbArticleList = dbOrder.getArticles();
        List<Article> articleList = new ArrayList<>();
        for (DBArticle dbArticle : dbArticleList) {
            Article article = new Article(dbArticle.getId(), dbArticle.getCount());
            article.setDelivered(dbArticle.isDelivered());
            articleList.add(article);
        }
        DBState dbState = dbOrder.getState();
        State state = new State();
        state.setArticlesReady(dbState.isArticlesReady());
        state.setCustomerReady(dbState.isCustomerReady());
        state.setCancelled(dbState.isCancelled());
        state.setValid(dbState.isValid());
        state.setDelivered(dbState.isDelivered());
        return new Order(dbOrder.getId(), dbOrder.getDate(), dbOrder.getStoreId(), dbOrder.getCustomerId(), dbOrder.getEmployeeId(), state, articleList);
    }
}
