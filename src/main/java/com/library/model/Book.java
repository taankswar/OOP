package com.library.model;

/**
 * Книга. Равенство определяется по ISBN (гарантируется record).
 */
public record Book(String isbn, String title, Author author, Genre genre) {
    @Override
    public String toString() {
        return "\"" + title + "\" by " + author + " [" + isbn + "]";
    }
}