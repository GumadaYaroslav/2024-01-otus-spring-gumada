package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        return new ArrayList<>();
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
        //...
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
                INSERT INTO books (title, author_id, genre_id)
                VALUES (:book_title, :author_id, :genre_id)
                """;
        jdbc.update(sqlQuery, parameterSource, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        //...
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
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
