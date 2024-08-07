package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;
    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author author) {
        var actualAuthor = jpaAuthorRepository.findById(author.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(author);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = jpaAuthorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsAll(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

}