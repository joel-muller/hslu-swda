package ch.hslu.swda.business;

import ch.hslu.swda.entities.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    public final static String DELIMITER = ";";
    public final static String FILENAME = "books.csv";
    public final static int FILECOUNT = 271380;
    private static final Logger LOG = LoggerFactory.getLogger(CSVReader.class);

    public static List<Book> getBooks() {
        List<Book> records = new ArrayList<>();
        int currentId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                records.add(Book.generateFromList(currentId, Arrays.asList(values)));
                currentId++;
            }
            if (currentId != FILECOUNT) throw new IOException("Not all books could be scanned");
        } catch (IOException e) {
            LOG.error("Error occurred while reading the file: {}", e.getMessage());
        }
        LOG.info("All articles have been successfully initialized");
        return records;
    }

}
