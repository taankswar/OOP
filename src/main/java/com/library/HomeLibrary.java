package com.library;

import com.library.model.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Главный класс домашней библиотеки.
 * Управляет коллекцией экземпляров книг, выполняет поиск, выдачу и формирует отчеты.
 */
public class HomeLibrary {
    private final List<BookCopy> copies;

    public HomeLibrary() {
        this.copies = new ArrayList<>();
    }

    /**
     * Добавляет новый экземпляр книги в библиотеку.
     */
    public void addCopy(BookCopy copy) {
        copies.add(copy);
    }

    /**
     * Поиск экземпляров по заданному критерию.
     * Использует switch-expression и pattern matching (Java 21).
     *
     * @param criteria критерий поиска (автор, название или жанр).
     * @return неизменяемый список найденных экземпляров.
     */
    public List<BookCopy> search(SearchCriteria criteria) {
        var result = switch (criteria) {
            case SearchCriteria.ByAuthor(var author) ->
                    copies.stream().filter(c -> c.getBook().author().equals(author)).toList();
            case SearchCriteria.ByTitle(var title) ->
                    copies.stream().filter(c -> c.getBook().title().equalsIgnoreCase(title)).toList();
            case SearchCriteria.ByGenre(var genre) ->
                    copies.stream().filter(c -> c.getBook().genre() == genre).toList();
        };
        return List.copyOf(result); // Инкапсуляция: возвращаем неизменяемую копию
    }

    /**
     * Возвращает полный адрес местоположения экземпляра.
     */
    public String getLocationOf(BookCopy copy) {
        return copy.getLocation().toString();
    }

    /**
     * Выдает экземпляр книги читателю.
     */
    public void issue(BookCopy copy, String reader, LocalDate date, int days) {
        copy.issueTo(reader, date, days);
    }

    /**
     * Принимает возврат экземпляра книги.
     */
    public void returnBook(BookCopy copy) {
        copy.returnBook();
    }

    /**
     * Возвращает список всех выданных на данный момент экземпляров.
     */
    public List<BookCopy> getIssuedCopies() {
        return copies.stream().filter(BookCopy::isIssued).toList();
    }

    /**
     * Возвращает список просроченных экземпляров на указанную дату.
     */
    public List<BookCopy> getOverdueCopies(LocalDate date) {
        return copies.stream()
                .filter(c -> c.isIssued() && c.getCurrentLoan().isOverdueOn(date))
                .toList();
    }

    // --- Повышенный уровень ---

    /**
     * Переносит полку (обновляет адреса всех экземпляров, находящихся на ней).
     */
    public void moveShelf(Location oldLoc, String newRoom, String newCabinet, String newShelf) {
        var newLocation = new Location(newRoom, newCabinet, newShelf);
        copies.stream()
                .filter(c -> c.getLocation().equals(oldLoc))
                .forEach(c -> c.setLocation(newLocation));
    }

    /**
     * Формирует отчет о занятости полок (количество экземпляров на каждой полке).
     */
    public Map<Location, Long> getShelfOccupancy() {
        return copies.stream().collect(Collectors.groupingBy(BookCopy::getLocation, Collectors.counting()));
    }

    /**
     * Формирует отчет о распределении фонда по жанрам.
     */
    public Map<Genre, Long> getGenreDistribution() {
        return copies.stream().collect(Collectors.groupingBy(c -> c.getBook().genre(), Collectors.counting()));
    }

    /**
     * Возвращает список всех экземпляров библиотеки.
     * Инкапсуляция: возвращается неизменяемое представление.
     */
    public List<BookCopy> getAllCopies() {
        return Collections.unmodifiableList(copies);
    }
}