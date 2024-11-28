package ch.hslu.swda.persistence;


import dev.morphia.annotations.Entity;

@Entity("State")
public class DBState {
    private boolean valid;
    private boolean articlesReady;
    private boolean customerReady;
    private boolean delivered;
    private boolean cancelled;

    public DBState() {
        this.valid = false;
        this.articlesReady = false;
        this.customerReady = false;
        this.delivered = false;
        this.cancelled = false;
    }

    public DBState(boolean valid, boolean articlesReady, boolean customerReady, boolean delivered, boolean cancelled) {
        this.valid = valid;
        this.articlesReady = articlesReady;
        this.customerReady = customerReady;
        this.delivered = delivered;
        this.cancelled = cancelled;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isArticlesReady() {
        return articlesReady;
    }

    public void setArticlesReady(boolean articlesReady) {
        this.articlesReady = articlesReady;
    }

    public boolean isCustomerReady() {
        return customerReady;
    }

    public void setCustomerReady(boolean customerReady) {
        this.customerReady = customerReady;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
