package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserService;
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
 * Indicates that Spring Boot controller class Comment is able to respond to
 * HTTP requests.
 */
@RestController
@RequestMapping("/histories/{historyId:\\d+}/comments")
@ExposesResourceFor(Comment.class)
public class CommentController {

  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;

  /**
   * Creates a new instance of comment class.
   *
   * @param historyRepository
   * @param userService
   * @param commentRepository
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
   * Retrieves a collection of comments that can be iterated based on the HistoryId.
   *
   * @param historyId for the retrieved comment.
   * @return comments based on historyId || an exception as NoSuchElementException.
   * @throws NoSuchElementException when the comment with that historyId is not found.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Comment> get(@PathVariable long historyId) {
    return historyRepository.findById(historyId)
        .map(History::getComments)
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Posts comments with the corresponding historyId and authentication.
   *
   * @param comment   posted on the server
   * @param historyId for posted comment
   * @param auth      for the user that adds the comment
   * @return saved comment with its Author and historyId
   * @throws NoSuchElementException when the comment with that historyId is not found.
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
   * Retrieves comments based on historyId and their Id.
   *
   * @param id        that comments have
   * @param historyId the Id that history has
   * @return find by id or else throw exception.
   * @throws NoSuchElementException when the comment with that historyId || that id is not found.
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment get(@PathVariable long historyId, @PathVariable long id) {
    return historyRepository.findById(historyId)
        .flatMap((h) -> commentRepository.findById(id))
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows comments to be modified.
   *
   * @param historyId history's id
   * @param id        comment's id
   * @param comment   text of the actual comments
   * @param auth      user's authentication
   * @return existing comment after it was modified
   * @throws NoSuchElementException when the comment is not found.
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
   * Deletes the comment.
   *
   * @param historyId history's id
   * @param id        comment's id
   * @param auth      user's authentication
   * @return null
   * @throws .orElse null
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

