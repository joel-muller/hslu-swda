package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.CentralWarehouseOrderJSONMapper;
import ch.hslu.swda.entities.CentralWarehouseOrderMapper;
import ch.hslu.swda.entities.OrderArticle;
import ch.hslu.swda.micro.OrderManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class MysqlDatabasePersistor implements CentralWarehouseOrderPersistor{
    private final DatabaseConnector databaseConnector;
    private Connection connection;
    private static final Logger LOG = LoggerFactory.getLogger(MysqlDatabasePersistor.class);

    private final CentralWarehouseOrderJSONMapper mapper = new CentralWarehouseOrderJSONMapper();

    public MysqlDatabasePersistor(DatabaseConnector databaseConnector){
    this.databaseConnector = databaseConnector;
    this.connection = databaseConnector.getConnection();

    }
    @Override
    public void save(CentralWarehouseOrder order) throws IOException{
        try{
        insertUpdateOrder(order);


        List<OrderArticle> orderArticles = order.getArticles();
        for(OrderArticle article :orderArticles){
            insertUpdateArticle(order.getId(),article);
        }

        }catch (SQLException e){
            LOG.error("Could not save order: "+ order.getId());
            reconnect();
        }

        LOG.info("saved order: "+ order.getId());
    }

    private void reconnect(){
        connection = databaseConnector.getConnection();
    }

    private void insertUpdateArticle(UUID warehouseOrder, OrderArticle article) throws SQLException {
        // Step 1: Retrieve the `id` from `warehouse_order`
        String getIdQuery = "SELECT id FROM warehouse_order WHERE uuid = ? LIMIT 1;";
        try (PreparedStatement getIdStatement = connection.prepareStatement(getIdQuery)) {
            getIdStatement.setString(1, warehouseOrder.toString());
            ResultSet resultSet = getIdStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");

                // Step 2: Perform the insert or update operation
                String insertUpdateQuery = """
                INSERT INTO warehouse_order_article (warehouse_order, article, count, fulfilled, next_delivery_date)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    count = VALUES(count),
                    fulfilled = VALUES(fulfilled),
                    next_delivery_date = VALUES(next_delivery_date);
                """;

                try (PreparedStatement insertUpdateStatement = connection.prepareStatement(insertUpdateQuery)) {
                    insertUpdateStatement.setInt(1, id);
                    insertUpdateStatement.setInt(2, article.getId());
                    insertUpdateStatement.setInt(3, article.getCount());
                    insertUpdateStatement.setInt(4, article.getFulfilled());
                    insertUpdateStatement.setDate(5,
                            article.getNextDeliveryDate() == null ? null : java.sql.Date.valueOf(article.getNextDeliveryDate()));

                    insertUpdateStatement.executeUpdate();
                }
            } else {
                throw new SQLException("Warehouse order ID not found for UUID: " + warehouseOrder);
            }
        }
    }
    private void insertUpdateOrder(CentralWarehouseOrder order)throws SQLException{
        String statement = """
            Insert into warehouse_order(uuid,store_id,customer_order_id,cancelled) VALUES(?,?,?,?) ON DUPLICATE key update
            cancelled = VALUES(cancelled)
            ;""";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1,order.getId().toString());
        preparedStatement.setString(2,order.getStoreId().toString());
        preparedStatement.setString(3,order.getCustomerOrderId()==null?null:order.getCustomerOrderId().toString());
        preparedStatement.setBoolean(4,order.getCancelled());

        preparedStatement.executeUpdate();

    }

    @Override
    public CentralWarehouseOrder getById(UUID id) throws IOException{

        String statement = """
            SELECT JSON_OBJECT('id',wo.uuid,'storeId',
            wo.store_id,'customerOrderId',
            wo.customer_order_id,'cancelled',IF(wo.cancelled = 1, TRUE, FALSE),
            'articles',JSON_ARRAYAGG(JSON_OBJECT('articleId',woa.article,
            'count',woa.count,
            'fulfilled',woa.fulfilled,
            'nextDeliveryDate',woa.next_delivery_date))) as CentralWarehouseOrder
            from warehouse_order wo
                left join warehouse_order_article woa on wo.id = woa.warehouse_order
                where wo.uuid=?
                GROUP BY wo.id
                LIMIT 1;
            ;""";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, id.toString());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return mapper.toCentralWarehouseOrder(rs.getString("CentralWarehouseOrder"));
            }else{
                return null;
            }

        }catch(SQLException e){
            LOG.error(e.getMessage());
            reconnect();
            throw new IOException(e.getMessage());
        }

    }

    @Override
    public List<CentralWarehouseOrder> getFirstOpen(Integer n) {
        return null;
    }

    @Override
    public List<CentralWarehouseOrder> getAllOpen() throws IOException {
        List<CentralWarehouseOrder> list = new ArrayList<CentralWarehouseOrder>();
        String statement = """
            SELECT JSON_OBJECT('id',wo.uuid,'storeId',
            wo.store_id,'customerOrderId',
            wo.customer_order_id,'cancelled',IF(wo.cancelled = 1, TRUE, FALSE),
            'articles',JSON_ARRAYAGG(JSON_OBJECT('articleId',woa.article,
            'count',woa.count,
            'fulfilled',woa.fulfilled,
            'nextDeliveryDate',woa.next_delivery_date))) as CentralWarehouseOrder
            from warehouse_order wo
                left join warehouse_order_article woa on wo.id = woa.warehouse_order
                where wo.id in (SELECT distinct wo2.id from warehouse_order wo2
                                join warehouse_order_article woa2 on wo2.id = woa2.warehouse_order
                                where woa2.fulfilled<woa2.count and wo2.cancelled = FALSE)
                GROUP BY wo.id;
            ;""";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(mapper.toCentralWarehouseOrder(rs.getString("CentralWarehouseOrder")));
            }
            return list;

        }catch(SQLException e){
            LOG.error(e.getMessage());
            reconnect();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public int getOpenOrderCount() {
        return 0;
    }

    private List<CentralWarehouseOrder> deserializeFromResultSet(ResultSet rs){
        return new ArrayList<CentralWarehouseOrder>();
    }
}
