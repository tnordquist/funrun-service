package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.model.entity.User.Role;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import edu.cnm.deepdive.funrun.service.UserService;
import java.security.AccessControlException;
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
 * This class contains static methods, with convenience annotations,
 * which control the histories posted by users. Histories may be posted, deleted, and retrieved.
 */
@RestController
@RequestMapping("/histories")
@ExposesResourceFor(History.class)
public class HistoryController {

  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;

  /**
   * Allows histories to be posted, retrieved, updated, and deleted.
   *
   * @param historyRepository, commentRepository, userRepository
   * @param userService
   * @return histories.
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
   * Allows histories to be retrieved.
   *
   * @param
   * @return users ordered by name ascending.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<History> get(Authentication auth) {
    User user = userService.getCurrentUser(auth);
    return historyRepository.getAllByUserOrderByStartDescEndDesc(user);
  }

  /**
   * Allows events to be posted.
   *
   * @param history
   * @return new histories
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
   * Allows histories to be retrieved.
   *
   * @param id
   * @return histries findById or throw exception
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public History get(@PathVariable long id) {
    return historyRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows histories to be updated.
   *
   * @param id, history
   * @return existing, updated histories
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
   * Allows histories to be deleted.
   *
   * @param id
   * @return history or throw exception
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

