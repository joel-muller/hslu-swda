package ch.hslu.swda.entities;

import dev.morphia.annotations.Entity;

import java.util.Objects;

@Entity("state")
public class State {
    private boolean valid;
    private boolean articlesReady;
    private boolean customerReady;
    private boolean delivered;
    private boolean cancelled;

    public State() {
        this.valid = false;
        this.articlesReady = false;
        this.customerReady = false;
        this.delivered = false;
        this.cancelled = false;
    }

    public boolean isReady() {
        return articlesReady && customerReady;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isArticlesReady() {
        return articlesReady;
    }

    public boolean isCustomerReady() {
        return customerReady;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public boolean isCancelled() {
        return cancelled;
    }


    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setArticlesReady(boolean articlesReady) {
        this.articlesReady = articlesReady;
    }

    public void setCustomerReady(boolean customerReady) {
        this.customerReady = customerReady;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State state)) return false;
        return valid == state.valid && articlesReady == state.articlesReady && customerReady == state.customerReady && delivered == state.delivered && cancelled == state.cancelled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid, articlesReady, customerReady, delivered, cancelled);
    }

    @Override
    public String toString() {
        return "State{" +
                "valid=" + valid +
                ", articlesReady=" + articlesReady +
                ", customerReady=" + customerReady +
                ", delivered=" + delivered +
                ", cancelled=" + cancelled +
                '}';
    }
}
