package com.library.model;

/**
 * Запечатанный интерфейс для критериев поиска книг.
 * Реализует полиморфизм через pattern matching в switch.
 */
public sealed interface SearchCriteria
        permits SearchCriteria.ByAuthor, SearchCriteria.ByTitle, SearchCriteria.ByGenre {

    record ByAuthor(Author author) implements SearchCriteria {}
    record ByTitle(String title) implements SearchCriteria {}
    record ByGenre(Genre genre) implements SearchCriteria {}
}