package com.library.model;

/**
 * Жанры с полями и телом константы.
 */
public enum Genre {
    FICTION("Художественная проза"),
    SCIENCE("Научная литература"),
    FANTASY("Фэнтези"),
    DETECTIVE("Детектив");

    private final String description;

    Genre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}