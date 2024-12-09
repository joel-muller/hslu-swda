package ch.hslu.swda.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    void createExampleStore() {
        Store store = new Store();
        store.addDefaultInventory();
        for (StoreArticle article : store.getCopyOfArticleList()) {
            System.out.println(article);
        }
    }
}