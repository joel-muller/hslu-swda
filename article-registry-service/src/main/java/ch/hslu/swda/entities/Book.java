package ch.hslu.swda.entities;

import java.util.List;
import java.util.Objects;

public class Book {
    private final int id;
    private final String isbn;
    private final String title;
    private final String author;
    private final String year;
    private final String publisher;
    private final String imageUrlS;
    private final String imageUrlM;
    private final String imageUrlL;
    private final Price price;

    public Book(final int id, String isbn, String title, String author, String year, String publisher, String imageUrlS, String imageUrlM, String imageUrlL, Price price) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.imageUrlS = imageUrlS;
        this.imageUrlM = imageUrlM;
        this.imageUrlL = imageUrlL;
        this.price = price;
    }

    public static Book generateFromList(int id, List<String> list, Price price) {
        return new Book(id, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), price);
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageUrlS() {
        return imageUrlS;
    }

    public String getImageUrlM() {
        return imageUrlM;
    }

    public String getImageUrlL() {
        return imageUrlL;
    }

    public Price getPrice() {
        return price;
    }

    public int getFranken() {
        return price.getFranken();
    }

    public int getRappen() {
        return price.getRappen();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
