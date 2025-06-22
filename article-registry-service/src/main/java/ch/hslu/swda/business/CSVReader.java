package ch.hslu.swda.business;

import ch.hslu.swda.entities.Book;
import ch.hslu.swda.entities.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    public final static String DELIMITER = ";";
    public final static String DELIMITER_PRICES = ",";
    public final static String FILENAME = "books.csv";
    public final static String FILENAME_PRICES = "prices.csv";
    public final static int MIN_ID = 100000;
    public final static int NUMBER_OF_ARTICLES = 271379;
    public final static int MAX_ID = MIN_ID + NUMBER_OF_ARTICLES - 1;
    private static final Logger LOG = LoggerFactory.getLogger(CSVReader.class);

    public static List<Book> getBooks() throws InterruptedException {
        List<Book> records = new ArrayList<>();
        Map<Integer, Price> prices = addPrices();
        int currentId = MIN_ID;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                records.add(Book.generateFromList(currentId, Arrays.asList(values), prices.get(currentId)));
                currentId++;
                count ++;
            }
        } catch (IOException e) {
            LOG.error("Error occurred while reading the file: {}", e.getMessage());
        }
        if (count != NUMBER_OF_ARTICLES) throw new RuntimeException("Not all books could be scanned only {} books" + currentId);
        LOG.info("All {} articles have been successfully initialized", NUMBER_OF_ARTICLES);
        return records;
    }

    private static Map<Integer, Price> addPrices() throws InterruptedException {
        Map<Integer, Price> prices = new HashMap<>();
        int currentId = MIN_ID;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME_PRICES))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER_PRICES);
                prices.put(currentId, Price.generateFromList(Arrays.asList(values)));
                currentId++;
                count++;
            }
        } catch (IOException e) {
            LOG.error("Error occurred while reading the prices: {}", e.getMessage());
        } catch (NumberFormatException e) {
            LOG.error("Parsing strings to price was not possible {}", e.getMessage());
        }
        if (count != NUMBER_OF_ARTICLES) throw new RuntimeException("Not all books could be scanned" + currentId);
        LOG.info("All {} prices have been successfully initialized", NUMBER_OF_ARTICLES);
        return prices;
    }
}
