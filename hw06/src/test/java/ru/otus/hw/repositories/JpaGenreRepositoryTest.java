package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {
    @Autowired
    private JpaGenreRepository repository;

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        List<Genre> actualGenres = repository.findAll();
        List<Genre> expectedGenres = getGenres();
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("Должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        Optional<Genre> actualGenre = repository.findById(expectedGenre.getId());
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);

    }

    private static List<Genre> getGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id)).toList();
    }

}