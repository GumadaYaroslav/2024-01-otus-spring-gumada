package ru.otus.hw.controllers.dto;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

public record BookDto(long id, String title, Author author, Genre genre) {
    public static Book toDomainObject(BookDto dto) {
        return new Book(dto.id(), dto.title(), dto.author(), dto.genre());
    }

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),book.getTitle(), book.getAuthor() ,book.getGenre());
    }
}
