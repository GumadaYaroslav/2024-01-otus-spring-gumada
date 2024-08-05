package ru.otus.hw.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.repositories.JpaBookRepositoryTest.getDbBooks;

@WebMvcTest({BookController.class, SecurityConfiguration.class})
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void shouldReturnCorrectBookListForRoleUser() {
        getAllBooks(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void shouldReturnCorrectBookListForRoleAdmin() {
        getAllBooks(status().isOk());
    }

    @Test
    public void shouldReturnCorrectBookListForNotAuthorise() {
        getAllBooks(redirectedUrlPattern("**/login"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void shouldShowCreateBookForRoleAdmin() {
        showCreateBookPage(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void shouldNotShowCreateBookForRoleUser() {
        showCreateBookPage(status().isForbidden());
    }

    @Test
    public void shouldNotShowCreateBookForNotAuthoriseAndRedirect() {
        showCreateBookPage(redirectedUrlPattern("**/login"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void shouldShowDeleteBookForRoleAdmin() {
        showDeleteBookPage(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void shouldNotShowDeleteBookForRoleUser() {
        showDeleteBookPage(status().isForbidden());
    }

    @Test
    public void shouldNotShowDeleteBookForNotAuthoriseAndRedirect() {
        showDeleteBookPage(redirectedUrlPattern("**/login"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void shouldShowEditBookForRoleAdmin() {
        showEditBookPage(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void shouldNotShowEditBookForRoleUser() {
        showEditBookPage(status().isForbidden());
    }

    @Test
    public void shouldNotShowEditBookForNotAuthoriseAndRedirect() {
        showEditBookPage(redirectedUrlPattern("**/login"));
    }

    @SneakyThrows
    private void showEditBookPage(ResultMatcher resultMatcher) {
        when(bookService.findById(1)).thenReturn(Optional.of(getDbBooks().get(0)));
        mvc.perform(get("/edit/1")).andExpect(resultMatcher);
    }

    @SneakyThrows
    private void showDeleteBookPage(ResultMatcher resultMatcher) {
        when(bookService.findById(1)).thenReturn(Optional.of(getDbBooks().get(0)));
        mvc.perform(get("/delete/1")).andExpect(resultMatcher);
    }

    @SneakyThrows
    private void showCreateBookPage(ResultMatcher resultMatcher) {
        mvc.perform(get("/create")).andExpect(resultMatcher);
    }

    @SneakyThrows
    private void getAllBooks(ResultMatcher resultMatcher) {
        when(bookService.findAll()).thenReturn(getDbBooks());
        mvc.perform(get("/")).andExpect(resultMatcher);
    }

}
