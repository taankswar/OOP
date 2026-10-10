package com.library.model;

/**
 * Местоположение: Комната -> Шкаф -> Полка.
 */
public record Location(String room, String cabinet, String shelf) {
    @Override
    public String toString() {
        return room + " / " + cabinet + " / " + shelf;
    }
}