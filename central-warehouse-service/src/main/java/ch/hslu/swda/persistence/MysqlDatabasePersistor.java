package ch.hslu.swda.persistence;

import ch.hslu.swda.entities.CentralWarehouseOrder;
import ch.hslu.swda.entities.CentralWarehouseOrderJSONMapper;
import ch.hslu.swda.entities.CentralWarehouseOrderMapper;
import ch.hslu.swda.entities.OrderArticle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class MysqlDatabasePersistor implements CentralWarehouseOrderPersistor{
private final Connection connection;

private final CentralWarehouseOrderJSONMapper mapper = new CentralWarehouseOrderJSONMapper();

    public MysqlDatabasePersistor(Connection connection){
    this.connection = connection;

    }
    @Override
    public void save(CentralWarehouseOrder order) {

    }

    private void insertUpdateArticle(Integer warehouseOrder, OrderArticle article)throws SQLException {

        String statement = """
            Insert into warehouse_order_article VALUES(?,?,?,?,?) ON DUPLICATE key update
            count = VALUES(count),
            fulfilled= VALUES(fulfilled),
            next_delivery_date = VALUES(next_delivery_date)
            ;""";

        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setInt(1,warehouseOrder);
        preparedStatement.setInt(2,article.getId());
        preparedStatement.setInt(3,article.getCount());
        preparedStatement.setInt(4,article.getFulfilled());
        preparedStatement.setDate(5,java.sql.Date.valueOf(article.getNextDeliveryDate()));

        preparedStatement.executeUpdate();


    }
    private void insertUpdateOrder(CentralWarehouseOrder order)throws SQLException{
        String statement = """
            Insert into warehouse_order(uuid,store_id,customer_order_id,cancelled) VALUES(?,?,?,?) ON DUPLICATE key update
            cancelled = VALUES(cancelled)
            ;""";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1,order.getId().toString());
        preparedStatement.setString(2,order.getStoreId().toString());
        preparedStatement.setString(3,order.getCustomerOrderId().toString());
        preparedStatement.setBoolean(4,order.getCancelled());

        preparedStatement.executeUpdate();

    }

    @Override
    public CentralWarehouseOrder getById(UUID id) throws IOException{

        String statement = """
            SELECT JSON_OBJECT(wo.uuid,'storeId',
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
            throw new IOException(e.getMessage());
        }

    }

    @Override
    public List<CentralWarehouseOrder> getFirstOpen(Integer n) {
        return null;
    }

    @Override
    public List<CentralWarehouseOrder> getAllOpen() {
        return null;
    }

    @Override
    public int getOpenOrderCount() {
        return 0;
    }

    private List<CentralWarehouseOrder> deserializeFromResultSet(ResultSet rs){
        return new ArrayList<CentralWarehouseOrder>();
    }
}
