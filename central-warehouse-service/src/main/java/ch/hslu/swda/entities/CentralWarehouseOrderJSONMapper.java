package ch.hslu.swda.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class CentralWarehouseOrderJSONMapper implements CentralWarehouseOrderMapper<String>{

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(CentralWarehouseOrderJSONMapper.class);

    @Override
    public  String fromCentralWarehouseOrder(CentralWarehouseOrder order) {

        try {
            // Creating a root JSON node
            ObjectNode rootNode = mapper.createObjectNode();

            // Setting basic fields
            rootNode.put("id", order.getId().toString());
            rootNode.put("storeId", order.getStoreId().toString());
            rootNode.put("customerOrderId", order.getCustomerOrderId()==null?null:order.getCustomerOrderId().toString());
            rootNode.put("cancelled", order.getCancelled());

            // Setting articles
            ArrayNode articlesNode = mapper.createArrayNode();
            for (OrderArticle article : order.getArticles()) {
                ObjectNode articleNode = mapper.createObjectNode();
                articleNode.put("id", article.getId());
                articleNode.put("count", article.getCount());
                articleNode.put("fulfilled", article.getFulfilled());
                articleNode.put("nextDeliveryDate", article.getNextDeliveryDate()== null?null:article.getNextDeliveryDate().toString());
                articlesNode.add(articleNode);
            }
            rootNode.set("articles", articlesNode);

            // Converting rootNode to JSON string
            return mapper.writeValueAsString(rootNode);

        } catch (JsonProcessingException e) {
            LOG.error("Error serializing CentralWarehouseOrder: {}", e.getMessage());
            return null;
        }
    }


    @Override
    public  CentralWarehouseOrder toCentralWarehouseOrder(String in){
        try {
            JsonNode root = mapper.readTree(in);
            UUID id = UUID.fromString(root.get("id").asText());
            UUID storeId = UUID.fromString(root.get("storeId").asText());
            UUID customerOrderId = UUID.fromString(root.get("customerOrderId").asText());
            boolean cancelled = root.get("cancelled").asBoolean();
            ArrayList<OrderArticle> articles = new ArrayList<OrderArticle>();

            for(JsonNode node : root.get("articles")){
                LOG.info(node.asText());
                int articleId = node.get("articleId").asInt();
                int count = node.get("count").asInt();
                int fulfilled = node.get("fulfilled").asInt();
                LocalDate nextDeliveryDate;
                if(node.get("nextDeliveryDate").isNull()){
                    nextDeliveryDate=null;
                }else
                    nextDeliveryDate = LocalDate.parse(node.get("nextDeliveryDate").asText());

                articles.add(new OrderArticle(articleId,count,fulfilled,nextDeliveryDate));
            }

            return new CentralWarehouseOrder(id,storeId,customerOrderId,cancelled,articles);

        }catch (JsonProcessingException e){
            LOG.error(e.getMessage());
            return null;
        }
    }
}
