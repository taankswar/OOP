package com.library.model;

/**
 * Автор книги. Неизменяемый.
 */
public record Author(String firstName, String lastName) {
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}