package ch.hslu.swda.business;

import ch.hslu.swda.entities.Book;
import ch.hslu.swda.entities.Validity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArticleHandler {
    public static int MIN_COUNT = 1;
    public static int MAX_COUNT = 100;
    private final List<Book> articles;
    private static final Logger LOG = LoggerFactory.getLogger(ArticleHandler.class);


    public ArticleHandler(List<Book> articles) {
        this.articles = articles;
    }

    private boolean checkArticles(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            if (CSVReader.MIN_ID > pair.getKey() || pair.getKey() > CSVReader.MAX_ID) {
                LOG.debug("Article id no valid");
                return false;
            }
            if (MIN_COUNT > pair.getValue() || pair.getValue() > MAX_COUNT) {
                LOG.debug("Count of article not valid");
                return false;
            }
        }
        return true;
    }

    public Validity generateValidity(Map<Integer, Integer> articles, UUID orderId) {
        if (articles.isEmpty() || !checkArticles(articles)) {
            return new Validity(orderId, false, new HashMap<>(), new HashMap<>());
        }
        Map<Integer, Integer> francs = new HashMap<>();
        Map<Integer, Integer> centimes = new HashMap<>();
        for (int id : articles.keySet()) {
            Book book = getBook(id);
            if (book == null) {
                // Throwing runtime exception because the article validity is given, so something has to be wrong with the list because the book should be here
                throw new RuntimeException("Error while loading a book");
            }
            francs.put(id, book.getFrancs());
            centimes.put(id, book.getCentimes());
        }
        return new Validity(orderId, true, francs, centimes);
    }

    protected Book getBook(int id) {
        for (Book book : articles) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

}
