package ch.hslu.swda.business;

import ch.hslu.swda.entities.Article;
import ch.hslu.swda.entities.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ArticleHandler {
    private final List<Book> articles;
    private static final Logger LOG = LoggerFactory.getLogger(ArticleHandler.class);


    public ArticleHandler() {
        this.articles = CSVReader.getBooks();
    }

    public void printArticles() {
        for (Book book : articles) {
            LOG.info(book.toString());
        }
    }

    public boolean checkArticles(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> pair : map.entrySet()) {
            if (0 > pair.getKey() || pair.getKey() > CSVReader.FILECOUNT) {
                LOG.debug("Article id no valid");
                return false;
            }
            if (1 > pair.getValue() || pair.getValue() > 100) {
                LOG.debug("Count of article not valid");
                return false;
            }
        }
        return true;
    }



}
