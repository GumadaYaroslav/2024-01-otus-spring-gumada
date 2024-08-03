package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentsConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isEmpty()) {
            return "Comment with id %s not found".formatted(id);
        }
        return commentsConverter.commentToString(comment.get());
    }

    @ShellMethod(value = "Insert new comment for book", key = "inc")
    public String insertComment(String text, long bookId) {
        Comment savedComment = commentService.insert(text, bookId);
        return commentsConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Update comment by ID", key = "ucid")
    public String updateComment(long id, String text) {
        Comment savedComment = commentService.update(id, text);
        return commentsConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Find all comments by book id", key = "fbbid")
    public String findCommentsByBookId(long bookId) {
        List<Comment> comments = commentService.findAllCommentsByBookId(bookId);
        return comments.stream()
                .map(commentsConverter::commentToString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Delete comment by id", key = "dcbid")
    public String deleteCommentById(long id) {
        commentService.deleteById(id);
        return "Comment with %d deleted".formatted(id);
    }
}
