package com.library;

import com.library.model.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Консольное приложение для демонстрации работы библиотеки.
 */
public class LibraryDemo {
    public static void main(String[] args) {
        var library = new HomeLibrary();

        System.out.println("=== 1. Наполнение библиотеки (создаем 30 экземпляров) ===");
        var authors = List.of(
                new Author("Айзек", "Азимов"),
                new Author("Джон", "Толкин"),
                new Author("Артур", "Конан Дойл")
        );

        // Генерируем 30 книг
        for (int i = 1; i <= 30; i++) {
            var author = authors.get(i % 3);
            var genre = Genre.values()[i % 4];
            var book = new Book("ISBN-" + i, "Книга №" + i, author, genre);
            var loc = new Location("Комната " + (i % 3 + 1), "Шкаф " + (i % 2 + 1), "Полка " + (i % 4 + 1));
            library.addCopy(new BookCopy(book, Condition.NEW, loc));
        }
        System.out.println("Всего экземпляров в библиотеке: " + library.getAllCopies().size() + "\n");

        System.out.println("=== 2. Поиск и адресация ===");
        var foundByAuthor = library.search(new SearchCriteria.ByAuthor(authors.get(0)));
        System.out.println("Найдено книг автора " + authors.get(0) + ": " + foundByAuthor.size() + " шт.");

        var copyToTest = library.getAllCopies().get(0);
        System.out.println("Где лежит '" + copyToTest.getBook().title() + "': " + library.getLocationOf(copyToTest) + "\n");

        System.out.println("=== 3. Выдача и возврат ===");
        library.issue(copyToTest, "Читатель А", LocalDate.now(), 14);
        System.out.println("Выдано книг всего: " + library.getIssuedCopies().size());

        library.returnBook(copyToTest);
        System.out.println("После возврата выдано: " + library.getIssuedCopies().size() + "\n");

        System.out.println("=== 4. Обработка исключений ===");
        library.issue(copyToTest, "Читатель Б", LocalDate.now(), 14);
        try {
            // Пытаемся выдать уже выданную книгу
            library.issue(copyToTest, "Читатель В", LocalDate.now(), 14);
        } catch (BookAlreadyIssuedException e) {
            // Осмысленная обработка (не пустой catch)
            System.out.println("Перехвачено ожидаемое исключение: " + e.getMessage());
        }
        library.returnBook(copyToTest); // Возвращаем для чистоты
        System.out.println();

        System.out.println("=== 5. Просроченные книги ===");
        var overdueCopy = library.getAllCopies().get(1);
        library.issue(overdueCopy, "Забывчивый читатель", LocalDate.now().minusDays(30), 14);
        var overdue = library.getOverdueCopies(LocalDate.now());
        System.out.println("Найдено просроченных книг: " + overdue.size() + "\n");

        System.out.println("=== 6. Повышенный уровень (Перестановка и отчеты) ===");
        var oldLoc = overdueCopy.getLocation();
        library.moveShelf(oldLoc, "Новая Комната", "Новый Шкаф", "Новая Полка");
        System.out.println("Новый адрес книги '" + overdueCopy.getBook().title() + "': " + library.getLocationOf(overdueCopy));

        System.out.println("\nОтчет о занятости полок (всего уникальных полок): " + library.getShelfOccupancy().size());
        System.out.println("Распределение фонда по жанрам:");
        library.getGenreDistribution().forEach((genre, count) ->
                System.out.println("  - " + genre.getDescription() + ": " + count + " шт.")
        );
    }
}