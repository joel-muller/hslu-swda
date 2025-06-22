package ch.hslu.swda.business;

import ch.hslu.swda.entities.Book;
import ch.hslu.swda.entities.Price;
import ch.hslu.swda.entities.Validity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArticleHandlerTest {
    private List<Book> books;
    private ArticleHandler articleHandler;

    @BeforeEach
    void setUp() {
        books = List.of(
                new Book(371378, "978-3-16-148410-0", "Book One", "Author A", "2001", "Publisher A", "small.jpg", "medium.jpg", "large.jpg", new Price(10, 50)),
                new Book(200000, "978-3-16-148411-7", "Book Two", "Author B", "2005", "Publisher B", "small2.jpg", "medium2.jpg", "large2.jpg", new Price(15, 25)),
                new Book(100000, "978-3-16-148412-4", "Book Three", "Author C", "2010", "Publisher C", "small3.jpg", "medium3.jpg", "large3.jpg", new Price(20, 75))
        );
        articleHandler = new ArticleHandler(books);
    }



    @Test
    void testGenerateValidityValidArticles() {
        Map<Integer, Integer> articleMap = Map.of(100000, 5, 371378, 10); // Sample article map
        UUID orderId = UUID.randomUUID();

        Validity validity = articleHandler.generateValidity(articleMap, orderId);

        assertTrue(validity.valid());
        assertEquals(orderId, validity.idOrder());
        assertEquals(2, validity.francsPerUnit().size());

        // Verify francsPerUnit values using the key from the map
        assertTrue(validity.francsPerUnit().containsKey(100000));
        assertTrue(validity.francsPerUnit().containsKey(371378));
        assertEquals(20, validity.francsPerUnit().get(100000));
        assertEquals(10, validity.francsPerUnit().get(371378));

        // Verify centimesPerUnit values using the key from the map
        assertTrue(validity.centimesPerUnit().containsKey(100000));
        assertTrue(validity.centimesPerUnit().containsKey(371378));
        assertEquals(75, validity.centimesPerUnit().get(100000));
        assertEquals(50, validity.centimesPerUnit().get(371378));
    }

    @Test
    void testGenerateValidityEmptyArticles()  {
        Map<Integer, Integer> articleMap = new HashMap<>();
        UUID orderId = UUID.randomUUID();

        Validity validity = articleHandler.generateValidity(articleMap, orderId);

        assertFalse(validity.valid());
        assertEquals(orderId, validity.idOrder());
        assertTrue(validity.francsPerUnit().isEmpty());
        assertTrue(validity.centimesPerUnit().isEmpty());
    }

    @Test
    void testGenerateValidityInvalidArticleId()  {
        Map<Integer, Integer> articleMap = Map.of(-1, 10); // Invalid article ID
        UUID orderId = UUID.randomUUID();

        Validity validity = articleHandler.generateValidity(articleMap, orderId);

        assertFalse(validity.valid());
        assertEquals(orderId, validity.idOrder());
        assertTrue(validity.francsPerUnit().isEmpty());
        assertTrue(validity.centimesPerUnit().isEmpty());
    }


    @Test
    void testGenerateValidityInvalidArticleCount()  {
        Map<Integer, Integer> articleMap = Map.of(100234, 101); // Count exceeds MAX_COUNT
        UUID orderId = UUID.randomUUID();

        Validity validity = articleHandler.generateValidity(articleMap, orderId);

        assertFalse(validity.valid());
        assertEquals(orderId, validity.idOrder());
        assertTrue(validity.francsPerUnit().isEmpty());
        assertTrue(validity.centimesPerUnit().isEmpty());
    }

    @Test
    void testGetBookValidId() {
        Book book = articleHandler.getBook(100000);
        assertNotNull(book);
        assertEquals(100000, book.getId());
        assertEquals("Book Three", book.getTitle());
    }

    @Test
    void testGetBookInvalidId() {
        Book book = articleHandler.getBook(999);

        assertNull(book);
    }
}