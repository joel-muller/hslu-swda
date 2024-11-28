package ch.hslu.swda.entities;

import java.util.List;
import java.util.Map;

public record OrderProcessed(Map<Integer, Integer> articlesHaveToGetOrdered, List<Integer> articlesReady) {
}
