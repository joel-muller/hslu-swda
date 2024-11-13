package ch.hslu.swda.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderArticle {
    private final int id;
    private int count;
    private int fulfilled;

    private static final Logger LOG = LoggerFactory.getLogger(OrderArticle.class);

    public OrderArticle(int id, int count) throws IllegalArgumentException{
        if(id <100000){
            LOG.error("Illegal articleId: "+id+". articleId must be >= 100'000.");
            throw new IllegalArgumentException("articleId < 100'000");
        }
        if(count <1){
            LOG.error("Illegal count for article: "+count+". Count must be >0");
            throw new IllegalArgumentException("Illegal count for article: "+count+". Count must be >0");
        }
        this.id = id;
        this.count = count;
        this.fulfilled = 0;
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

