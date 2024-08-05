package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.BookModifyDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/")
    public String getAllBooks(Model model) {
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::toDto)
                .toList();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/create")
    public String editBook(Model model) {
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new BookModifyDto());
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "create";
    }


    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookModifyDto bookUpdateDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.info(objectError.toString()));
            redirectAttributes.addFlashAttribute("book", bookUpdateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return "redirect:/create/";
        }
        bookService.insert(bookUpdateDto.getTitle(), bookUpdateDto.getAuthorId(), bookUpdateDto.getGenreId());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") long bookId, Model model) {
        if (!model.containsAttribute("book")) {
            Book book = bookService.findById(bookId).orElseThrow(EntityNotFoundException::new);
            BookModifyDto bookDto = BookModifyDto.toDto(book);
            model.addAttribute("book", bookDto);
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "edit";
    }


    @PostMapping("/edit")
    public String updateBook(@RequestParam("id") long bookId,
                             @Valid @ModelAttribute("book") BookModifyDto bookUpdateDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.error(objectError.toString()));
            redirectAttributes.addFlashAttribute("book", bookUpdateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return "redirect:/edit/" + bookId;
        }
        bookService.update(bookId, bookUpdateDto.getTitle(), bookUpdateDto.getAuthorId(), bookUpdateDto.getGenreId());
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", book);
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id, Model model) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("%s. Entity not found.".formatted(ex.getMessage()));
    }
}
