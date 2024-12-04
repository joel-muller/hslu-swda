package ch.hslu.swda.messagesIngoing;

import ch.hslu.swda.entities.Article;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateOrder(Map<Integer, Integer> articles, UUID storeId, UUID customerId, UUID employeeId) { }