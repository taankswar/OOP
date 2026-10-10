package com.library;

import com.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class HomeLibraryTest {
    private HomeLibrary library;
    private Book book1;
    private BookCopy copy1;

    @BeforeEach
    void setUp() {
        library = new HomeLibrary();
        var author = new Author("Айзек", "Азимов");
        book1 = new Book("978-5-01-002543-4", "Основание", author, Genre.FANTASY);
        copy1 = new BookCopy(book1, Condition.NEW, new Location("Кабинет", "Шкаф 1", "Полка 2"));
        library.addCopy(copy1);
    }

    // 1. Поиск по автору
    @Test
    void testSearchByAuthor() {
        var results = library.search(new SearchCriteria.ByAuthor(book1.author()));
        assertEquals(1, results.size());
    }

    // 2. Поиск по названию
    @Test
    void testSearchByTitle() {
        var results = library.search(new SearchCriteria.ByTitle("Основание"));
        assertEquals(1, results.size());
    }

    // 3. Поиск по жанру
    @Test
    void testSearchByGenre() {
        var results = library.search(new SearchCriteria.ByGenre(Genre.FANTASY));
        assertEquals(1, results.size());
    }

    // 4. Выдача и возврат
    @Test
    void testIssueAndReturn() {
        library.issue(copy1, "Иванов", LocalDate.now(), 14);
        assertTrue(copy1.isIssued());

        library.returnBook(copy1);
        assertFalse(copy1.isIssued());
        assertEquals(1, copy1.getHistory().size());
    }

    // 5. Исключение при повторной выдаче (assertThrows)
    @Test
    void testIssueAlreadyIssuedThrowsException() {
        library.issue(copy1, "Иванов", LocalDate.now(), 14);
        var ex = assertThrows(BookAlreadyIssuedException.class, () ->
                library.issue(copy1, "Петров", LocalDate.now(), 14));
        assertTrue(ex.getMessage().contains("уже выдан"));
    }

    // 6. Исключение при возврате невыданной книги
    @Test
    void testReturnNotIssuedThrowsException() {
        var ex = assertThrows(BookNotIssuedException.class, () -> library.returnBook(copy1));
        assertTrue(ex.getMessage().contains("не находится в выдаче"));
    }

    // 7. Просроченные книги
    @Test
    void testOverdueBooks() {
        library.issue(copy1, "Иванов", LocalDate.now().minusDays(20), 14);
        var overdue = library.getOverdueCopies(LocalDate.now());
        assertEquals(1, overdue.size());
    }

    // 8. Перестановка (Повышенный уровень)
    @Test
    void testMoveShelf() {
        var oldLoc = copy1.getLocation();
        library.moveShelf(oldLoc, "Спальня", "Стеллаж", "Полка 1");
        assertEquals("Спальня / Стеллаж / Полка 1", library.getLocationOf(copy1));
    }

    // 9. Контракт equals/hashCode для record Book (Рефлексивность и симметричность)
    @Test
    void testBookEqualsAndHashCodeContract() {
        var author = new Author("Айзек", "Азимов");
        var book2 = new Book("978-5-01-002543-4", "Основание", author, Genre.FANTASY);

        assertEquals(book1, book2); // Симметричность
        assertEquals(book1, book1); // Рефлексивность
        assertEquals(book1.hashCode(), book2.hashCode()); // Хеш-коды равны
    }

    // 10. Проверка неравенства книг с разным ISBN
    @Test
    void testBookNotEqualsDifferentIsbn() {
        var book2 = new Book("978-0-00-000000-0", "Основание", book1.author(), Genre.FANTASY);
        assertNotEquals(book1, book2);
    }

    // 11. Проверка инкапсуляции (возвращается неизменяемый список)
    @Test
    void testUnmodifiableListReturned() {
        var allCopies = library.getAllCopies();
        assertThrows(UnsupportedOperationException.class, () -> allCopies.add(copy1));
    }

    // 12. Отчет о занятости полок
    @Test
    void testShelfOccupancyReport() {
        var occupancy = library.getShelfOccupancy();
        assertEquals(1L, occupancy.get(copy1.getLocation()));
    }
}