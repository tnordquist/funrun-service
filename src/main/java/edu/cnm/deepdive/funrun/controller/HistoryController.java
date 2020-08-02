package edu.cnm.deepdive.funrun.controller;

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
 * Contains static methods, with convenience annotations, which control the histories posted by
 * users. Histories may be posted, deleted, and retrieved.
 */
@RestController
@RequestMapping("/histories")
@ExposesResourceFor(History.class)
public class HistoryController {

  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;

  /**
   * Creates a new instance of history class.
   *
   * @param historyRepository
   * @param commentRepository
   * @param userService
   */
  @Autowired
  public HistoryController(HistoryRepository historyRepository,
      CommentRepository commentRepository,
      UserService userService) {
    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;

    this.userService = userService;
  }

  /**
   * Retrieves a collection of histories that can be iterated based on the auth.
   *
   * @param auth user's authentication
   * @return histories by start and end descending based on user.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<History> get(Authentication auth) {
    User user = userService.getCurrentUser(auth);
    return historyRepository.getAllByUserOrderByStartDescEndDesc(user);
  }

  /**
   * Posts history with the corresponding authentication.
   *
   * @param history to be posted
   * @param auth    user's authentication
   * @return new history
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<History> post(@RequestBody History history, Authentication auth) {
    User user = userService.getCurrentUser(auth);
    history.setUser(user);
    historyRepository.save(history);
    return ResponseEntity.created(history.getHref()).body(history);
  }

  /**
   * Allows histories to be retrieved by Id.
   *
   * @param id of history
   * @return history by Id
   * @throws NoSuchElementException
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public History get(@PathVariable long id) {
    return historyRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows histories to be modified.
   *
   * @param id      of history
   * @param history to be modified
   * @param auth    user's authentication
   * @return existing, modified history
   * @throws NoSuchElementException
   */
  @PutMapping(value = "/{id:\\d+}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public History put(@PathVariable long id, @RequestBody History history, Authentication auth) {
    User user = userService.getCurrentUser(auth);
    return historyRepository.findById(id)
        .map((existing) -> {
          userService.requireAccess(user, existing.getUser().getId());
          if (history.getStart() != null) {
            existing.setStart(
                history.getStart());
          }
          return historyRepository.save(existing);
        })
        .orElseThrow(NoSuchElementException::new);
  }


  /**
   * Allows history to be deleted.
   *
   * @param id of history
   * @return null
   * @throws .orElse null
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id, Authentication auth) {
    User user = userService.getCurrentUser(auth);
    historyRepository.findById(id)
        .map((history) -> {
          userService.requireAccess(user, history.getUser().getId());
          historyRepository.delete(history);
          return null;
        })
        .orElse(null);

  }

}

