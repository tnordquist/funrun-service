package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@ExposesResourceFor(Comment.class)
public class CommentController {


  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;

  @Autowired
  public CommentController(
      HistoryRepository historyRepository,
      CommentRepository commentRepository) {

    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Comment> get() {
    return commentRepository.getAllByOrderByAuthorAsc();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Comment> post(@RequestBody Comment comment) {
    commentRepository.save(comment);
    return ResponseEntity.created(comment.getHref()).body(comment);
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment get(@PathVariable long id) {
    return commentRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment put(@PathVariable long id, @RequestBody Comment comment) {
    Comment existingComment = get(id);
    if (comment.getAuthor() != null) {
      existingComment.setAuthor(comment.getAuthor());
    }
    return commentRepository.save(existingComment);

  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    historyRepository.findById(id)
        .map((history) -> {
          List<Comment> comments = history.getComments();
          comments.forEach((comment) -> comment.setHistory(null));
          commentRepository.saveAll(comments);
          return history;
        })
        .map((history) -> {
          historyRepository.delete(history);
          return history;

        })
        .orElseThrow(NoSuchElementException::new);
  }
  }

