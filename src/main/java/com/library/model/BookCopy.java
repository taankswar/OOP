package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Экземпляр книги (физический объект).
 * Содержит изменяемое состояние: местоположение, статус выдачи, историю.
 */
public class BookCopy {
    private final Book book;
    private Condition condition;
    private Location location;
    private Loan currentLoan;
    private final List<Loan> history;

    public BookCopy(Book book, Condition condition, Location location) {
        this.book = book;
        this.condition = condition;
        this.location = location;
        this.currentLoan = null;
        this.history = new ArrayList<>();
    }

    // --- Геттеры и сеттеры ---

    public Book getBook() {
        return book;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Обновляет местоположение экземпляра (используется при перестановке).
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    public Loan getCurrentLoan() {
        return currentLoan;
    }

    public boolean isIssued() {
        return currentLoan != null;
    }

    // --- Бизнес-логика ---

    /**
     * Выдает экземпляр книги читателю.
     * @throws BookAlreadyIssuedException если экземпляр уже выдан.
     */
    public void issueTo(String reader, LocalDate issueDate, int daysToReturn) {
        if (isIssued()) {
            throw new BookAlreadyIssuedException(
                    "Экземпляр книги '" + book.title() + "' (ISBN: " + book.isbn() + ") уже выдан читателю " + currentLoan.readerName() + "!"
            );
        }
        this.currentLoan = new Loan(reader, issueDate, issueDate.plusDays(daysToReturn));
    }

    /**
     * Возвращает экземпляр книги в библиотеку.
     * @throws BookNotIssuedException если экземпляр не был выдан.
     */
    public void returnBook() {
        if (!isIssued()) {
            throw new BookNotIssuedException(
                    "Экземпляр книги '" + book.title() + "' не находится в выдаче!"
            );
        }
        history.add(currentLoan);
        this.currentLoan = null;
    }

    /**
     * Возвращает историю выдач экземпляра.
     * Инкапсуляция: возвращается неизменяемое представление списка.
     */
    public List<Loan> getHistory() {
        return Collections.unmodifiableList(history);
    }

    @Override
    public String toString() {
        return "Экземпляр [" + book + "] в " + location + " (Состояние: " + condition + ")";
    }
}