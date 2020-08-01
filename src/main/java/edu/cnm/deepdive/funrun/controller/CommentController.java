package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains static methods, with convenience annotations, which indicates that the
 * annotated method (in a Spring Boot controller class) is able to respond to an HTTP GET request.
 */

@RestController
@RequestMapping("/histories/{historyId:\\d+}/comments")
@ExposesResourceFor(Comment.class)
public class CommentController {

  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;

  /**
   * Allows comments to be posted, retrieved, updated, and deleted.
   *
   * @param historyRepository, commentRepository
   * @param userService
   * @return comments.
   */
  @Autowired
  public CommentController(
      HistoryRepository historyRepository,
      CommentRepository commentRepository, UserService userService) {

    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;
    this.userService = userService;
  }

  /**
   * Retrieves comments.
   *
   * @param
   * @return comments ordered by author.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Comment> get(@PathVariable long historyId) {
    return historyRepository.findById(historyId)
        .map(History::getComments)
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Posts comments.
   *
   * @param comment
   * @return comments.
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Comment> post(@PathVariable long historyId, @RequestBody Comment comment,
      Authentication auth) {
    User user = userService.getCurrentUser(auth);
    return historyRepository.findById(historyId)
        .map((history) -> {
          comment.setAuthor(user);
          comment.setHistory(history);
          commentRepository.save(comment);
          return ResponseEntity.created(comment.getHref()).body(comment);
        })
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Gets comments.
   *
   * @param id
   * @return find by id or else throw exception.
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment get(@PathVariable long historyId, @PathVariable long id) {
    return historyRepository.findById(historyId)
        .flatMap((h) -> commentRepository.findById(id))
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows comments to be posted, retrieved, and deleted.
   *
   * @param id, comment
   * @return existingComment.
   */
  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment put(@PathVariable long historyId, @PathVariable long id,
      @RequestBody Comment comment, Authentication auth) {
    User user = userService.getCurrentUser(auth);
    return historyRepository.findById(historyId)
        .flatMap((h) -> commentRepository.findById(id))
        .map((existing) -> {
          if (comment.getAuthor() != null) {
            existing.setAuthor(comment.getAuthor());
          }
          return commentRepository.save(existing);
        })
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Deletes comments.
   *
   * @param id
   * @return history or else throw exception.
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long historyId, @PathVariable long id, Authentication auth) {
    historyRepository.findById(id)
        .flatMap((history) -> commentRepository.findById(id))
        .map((comment) -> {
          userService.requireAccess(userService.getCurrentUser(auth), comment.getAuthor().getId());
          commentRepository.delete(comment);
          return null;
        })
        .orElse(null);
  }
}

