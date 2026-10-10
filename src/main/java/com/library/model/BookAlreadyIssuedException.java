package com.library.model;

/**
 * Исключение, выбрасываемое при попытке выдать уже выданный экземпляр.
 */
public class BookAlreadyIssuedException extends RuntimeException {
    public BookAlreadyIssuedException(String message) {
        super(message);
    }
}