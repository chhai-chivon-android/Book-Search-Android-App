package com.example.android.booklisting;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    private List <String> authors;
    private String publisher;
    private String title;
    private String subtitle;

    private String isbn10;
    private String isbn13;

    private String textDescription;
    private String textsnippet;

    private String canonicalUrl;

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    private String publishedDate;

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }


    public Book(List <String> authors, String title) {
        this.authors = authors;
        this.title = title;
    }

    public Book(List <String> authors, String publisher, String title) {
        this.authors = authors;
        this.publisher = publisher;
        this.title = title;
    }

    public List <String> getAuthors() {
        return authors;
    }

    public void setAuthors(List <String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getTextsnippet() {
        return textsnippet;
    }

    public void setTextsnippet(String textsnippet) {
        this.textsnippet = textsnippet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!getAuthors().equals(book.getAuthors())) return false;
        if (!getTitle().equals(book.getTitle())) return false;
        if (getIsbn10() != null ? !getIsbn10().equals(book.getIsbn10()) : book.getIsbn10() != null)
            return false;
        return getIsbn13() != null ? getIsbn13().equals(book.getIsbn13()) : book.getIsbn13() == null;
    }

    @Override
    public int hashCode() {
        int result = getAuthors().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + (getIsbn10() != null ? getIsbn10().hashCode() : 0);
        result = 31 * result + (getIsbn13() != null ? getIsbn13().hashCode() : 0);
        return result;
    }
}