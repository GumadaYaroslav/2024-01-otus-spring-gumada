package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        String findByIdQuery = """
                select
                    b.id AS book_id,
                    b.title AS book_title,
                    a.id AS author_id,
                    a.full_name AS author_name,
                    g.id AS genre_id,
                    g.name AS genre_name
                from books b
                left join authors a on a.id = b.author_id
                left join genres g on g.id = b.genre_id
                where b.id = :id
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<Book> query = jdbc.query(findByIdQuery, parameterSource, new BookRowMapper());
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return query.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
                select 
                    b.id as id,
                    b.title as title,
                    b.author_id as author_id,
                    b.genre_id as genre_id,
                    a.full_name as author_name,
                    g.name as genre_name
                from books b
                left join authors a ON b.author_id = a.id
                left join genres g ON b.genre_id = g.id;
                """, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Optional<Book> book = findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException(String.format("Book with id = %s not found", id));
        }
        jdbc.update("delete from books where id = :id",params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(
                Map.of("id", book.getId(),
                        "book_title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId())
        );
        String sqlQuery = """
                insert into books (title, author_id, genre_id)
                values (:book_title, :author_id, :genre_id)
                """;
        jdbc.update(sqlQuery, parameterSource, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        Optional<Book> checkBook = findById(book.getId());
        if (checkBook.isEmpty()) {
            throw new EntityNotFoundException(String.format("Book with id = %s not found", book.getId()));
        }
        jdbc.update("""
                update books 
                set title = :title, author_id = :author_id, genre_id = :genre_id
                where id = :id
                """, params);
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            Author author = new Author(rs.getLong("author_id"), rs.getString("author_name"));
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }
}
