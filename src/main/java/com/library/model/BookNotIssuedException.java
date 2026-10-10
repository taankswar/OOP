package com.library.model;

/**
 * Исключение, выбрасываемое при попытке вернуть невыданный экземпляр.
 */
public class BookNotIssuedException extends RuntimeException {
    public BookNotIssuedException(String message) {
        super(message);
    }
}