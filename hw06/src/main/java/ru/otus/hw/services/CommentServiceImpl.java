package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final String BOOK_NOT_FOUND_EXCEPTION_TEMPLATE = "No book with id %s";
    private static final String COMMENT_NOT_FOUND_EXCEPTION_TEMPLATE = "No comment with id %s";

    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Override
    public List<Comment> findAllCommentsByBookId(long bookId) {
        return commentRepository.getAllCommentsByBookId(bookId);
    }

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public Comment insert(String text, long bookId) {
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_EXCEPTION_TEMPLATE
                        .formatted(bookId)));
        return commentRepository.save(new Comment(0, text, book));
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_EXCEPTION_TEMPLATE.formatted(id)));
        Book book = bookService.findById(comment.getId())
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_EXCEPTION_TEMPLATE
                        .formatted(comment.getBook().getId())));
        return commentRepository.save(new Comment(id, text, book));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
