package ru.otus.hw.controllers;

import org.assertj.core.groups.Tuple;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.BookModifyDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.hw.repositories.JpaBookRepositoryTest.getDbBooks;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;

    @Test
    @SneakyThrows
    void shouldGetAllBooks() {
        List<Book> books = getDbBooks();
        List<BookDto> expectedResult = books.stream().map(BookDto::toDto).toList();
        given(bookService.findAll()).willReturn(books);
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andReturn();
        List<BookDto> actual = (List<BookDto>) result.getModelAndView().getModel().get("books");
        List<Tuple> expectedResultTuples = expectedResult.stream()
                .map(e -> new Tuple(e.title(), e.id()))
                .toList();
        assertThat(actual)
                .extracting(BookDto::title, BookDto::id)
                .containsExactly(expectedResultTuples.get(0),
                        expectedResultTuples.get(1),
                        expectedResultTuples.get(2));
    }

    @Test
    public void shouldReturnCorrectBookById() throws Exception {
        List<Book> books = getDbBooks();
        BookModifyDto expectedResult = BookModifyDto.toDto(books.get(0));
        given(bookService.findById(1)).willReturn(Optional.of(books.get(0)));
        MvcResult result = mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("book"))
                .andReturn();
        BookModifyDto actual = (BookModifyDto) result.getModelAndView().getModel().get("book");
        assertThat(actual).matches(a -> a.getTitle().equals(expectedResult.getTitle()) &&
                Objects.equals(a.getId(), expectedResult.getId()));
    }

    @Test
    public void shouldTrowErrorIfBookDontExist() throws Exception {
        mockMvc.perform(get("/edit/500"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void shouldCorrectAddNewBook() throws Exception {
        BookModifyDto expectedResult = new BookModifyDto(0L, "BookTitle_4", 3, 3);
        mockMvc.perform(post("/create")
                        .flashAttr("book", expectedResult))
                .andExpect(status().is(302));
        verify(bookService, times(1)).insert(expectedResult.getTitle(),
                expectedResult.getAuthorId(),
                expectedResult.getGenreId());
    }

    @Test
    public void shouldCorrectUpdateBook() throws Exception {
        BookModifyDto expectedResult = new BookModifyDto(2L, "BookTitleUpdate", 3, 3);
        when(bookService.findById(expectedResult.getId())).thenReturn(Optional.of(new Book()));
        mockMvc.perform(post("/edit")
                        .param("id", "2")
                        .flashAttr("book", expectedResult))
                .andExpect(status().is(302));

        verify(bookService, times(1)).update(expectedResult.getId(),
                expectedResult.getTitle(),
                expectedResult.getAuthorId(),
                expectedResult.getGenreId());
    }

    @Test
    public void shouldTrowBadRequestIfUpdateBookUseNotExistingAuthorOrGenre() throws Exception {
        BookModifyDto invalidAuthorIdBook = new BookModifyDto(2L, "BookTitleUpdate", 500, 3);
        mockMvc.perform(post("/edit")
                        .param("id", "2")
                        .flashAttr("book", invalidAuthorIdBook))
                .andExpect(status().isBadRequest());
        BookModifyDto invalidGenreIdBook = new BookModifyDto(2L, "BookTitleUpdate", 2, 500);
        mockMvc.perform(post("/edit")
                        .param("id", "2")
                        .flashAttr("book", invalidGenreIdBook))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldTrowBadRequestIfUpdateBookDontExist() throws Exception {
        BookModifyDto expectedResult = new BookModifyDto(2L, "BookTitleUpdate", 3, 3);
        mockMvc.perform(post("/edit")
                        .param("id", "500")
                        .flashAttr("book", expectedResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCorrectDeleteBookById() throws Exception {
        when(bookService.findById(1)).thenReturn(Optional.of(new Book()));
        mockMvc.perform(post("/delete?id=1"))
                .andExpect(status().is(302));
        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    public void shouldTrowBadRequestIfDeletingNotExistingBook() throws Exception {
        mockMvc.perform(post("/delete?id=500"))
                .andExpect(status().isBadRequest());
    }
}