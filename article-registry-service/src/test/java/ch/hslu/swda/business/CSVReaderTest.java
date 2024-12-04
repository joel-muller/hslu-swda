package ch.hslu.swda.business;

import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swda.entities.Book;
import ch.hslu.swda.entities.Price;
import org.junit.jupiter.api.Test;

import java.util.List;

class CSVReaderTest {
    @Test
    void testCSVReader() {
        try {
            List<Book> bookList = CSVReader.getBooks();
            assertEquals(bookList.size(), CSVReader.NUMBER_OF_ARTICLES);

            assertEquals(bookList.get(100343), new Book(200343, "1563524872", "The Terrible Truth About Liberals", "", "", "", "", "", "", new Price(0, 0)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}