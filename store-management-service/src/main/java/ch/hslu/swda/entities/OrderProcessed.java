package ch.hslu.swda.entities;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OrderProcessed(UUID orderId, Map<Integer, Integer> articlesHaveToGetOrdered, List<Integer> articlesReady) {
}
