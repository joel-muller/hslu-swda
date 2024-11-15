package ch.hslu.swda.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class OrderArticle {
    private int id;
    private int count;
    private int fulfilled;
    private LocalDate nextDeliveryDate = null;

    private static final Logger LOG = LoggerFactory.getLogger(OrderArticle.class);

    public OrderArticle(int id, int count) throws IllegalArgumentException{
        this.setId(id);
        this.setCount(count);
        this.setFulfilled(0);
    }

    public OrderArticle(int id, int count,int fulfilled, LocalDate nextDeliveryDate) throws IllegalArgumentException{
        this.setId(id);
        this.setCount(count);
        this.setFulfilled(fulfilled);
        this.setNextDeliveryDate(nextDeliveryDate);
    }

    public int getId(){
        return this.id;
    }
    public int getCount() {
        return count;
    }
    public int getFulfilled(){
        return fulfilled;
    }

    private void setId(int id) throws IllegalArgumentException{
        if(id <100000){
            LOG.error("Illegal articleId: "+id+". articleId must be >= 100'000.");
            throw new IllegalArgumentException("articleId < 100'000");
        }
        this.id = id;
    }
    private void setCount(int count) throws IllegalArgumentException{
        if(count <1){
            LOG.error("Illegal count for article: "+count+". Count must be >0");
            throw new IllegalArgumentException("Illegal count for article: "+count+". Count must be >0");
        }
        this.count = count;
    }

    public LocalDate getNextDeliveryDate(){
        return this.nextDeliveryDate;
    }
    public void setNextDeliveryDate(LocalDate date) throws IllegalArgumentException{
        if(date == null){
            this.nextDeliveryDate= null;
            return;
        }
        if(date.isBefore(LocalDate.now())){
            LOG.error("Illegal count for article: "+count+". Count must be >0");
            throw new IllegalArgumentException("Cannot set next delivery Date into the past. Received LocalDate: "+ date.toString());
        }
        this.nextDeliveryDate = date;
    }

    public void setFulfilled(int fulfilled)throws IllegalArgumentException{
        if(fulfilled<0){
            LOG.error("Cannot Set fulfilled < 0. Actual value: "+ fulfilled);
            throw new IllegalArgumentException("Cannot Set fulfilled < 0. Actual value: "+ fulfilled);
        }
        if(fulfilled > this.count){
            throw new IllegalArgumentException("Cannot Set fulfilled > ordered count.");
        }
        this.fulfilled = fulfilled;
    }

}

