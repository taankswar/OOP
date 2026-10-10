package com.library.model;

import java.time.LocalDate;

/**
 * Факт выдачи книги.
 */
public record Loan(String readerName, LocalDate issueDate, LocalDate dueDate) {
    /**
     * Проверяет, просрочена ли выдача на указанную дату.
     */
    public boolean isOverdueOn(LocalDate date) {
        return date.isAfter(dueDate);
    }
}