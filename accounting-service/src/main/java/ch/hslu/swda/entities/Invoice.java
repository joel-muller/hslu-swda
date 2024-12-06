package ch.hslu.swda.entities;

import java.util.Map;
import java.util.UUID;

/**
 * A record class for sending the invoice details to the accounting service after an order is created
 * @param orderId the id of the order
 * @param customerId the id of the customer
 * @param employeeId the id of the employee
 * @param storeId the id of the store
 * @param articlesCount a map, where the key is the article id and the value is the count for how many times the article is ordered in the order
 * @param articlesPrices a map, where the key is the article id and the value is the price for how much the article multiplied with the count costs in a string with a format for example (15.45) for 15 Francs and 45 Centimes
 * @param totalPrice the price of all articles prices summed up and in the format for example 55.65 (55 Francs and 65 centimes)
 */
public record Invoice(UUID orderId, UUID customerId, UUID employeeId, UUID storeId, Map<Integer, Integer> articlesCount, Map<Integer, String> articlesPrices, String totalPrice) {
}
