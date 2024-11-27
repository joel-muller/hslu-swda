package ch.hslu.swda.persistence;

import dev.morphia.annotations.Id;

public class DBOrderArticle {
    @Id
    private int id;
    private int count;
    private boolean ready;

    public DBOrderArticle(final int id, final int count, final boolean ready) {
        this.id = id;
        this.count = count;
        this.ready = ready;
    }

    public DBOrderArticle() {
        this.id = -1;
        this.count = -1;
        this.ready = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
