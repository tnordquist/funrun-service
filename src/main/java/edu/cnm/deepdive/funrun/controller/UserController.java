package edu.cnm.deepdive.funrun.controller;


import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.model.entity.User.Role;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import edu.cnm.deepdive.funrun.service.UserService;
import java.security.AccessControlException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
@RequestMapping("/users")
@ExposesResourceFor(User.class)
public class UserController {

  private final UserRepository userRepository;
  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;

  /**
   * Allows users to be updated, retrieved, and deleted.
   *
   * @param historyRepository, commentRepository, userRepository
   * @return user.
   */
  public UserController(UserRepository userRepository,
      HistoryRepository historyRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;
  }

  /**
   * Retrieves users.
   *
   * @param
   * @return users ordered by name ascending.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<User> get() {
    return userRepository.getAllByOrderByNameAsc();
  }

  /**
   * Retrieves comments.
   *
   * @param id
   * @return users findById or throw exception.
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get(@PathVariable long id) {
    return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  /**
   * Updates users.
   *
   * @param id, user
   * @return existing user updated.
   */
  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public User put(@PathVariable long id, @RequestBody User user, Authentication auth) {
    User current = (User) auth.getPrincipal();
    if (id != current.getId() && current.getRole() != Role.ADMINISTRATOR) {
      throw new AccessControlException("");
    }
    return userRepository.findById(id)
        .map((existing) -> {
          if (user.getDisplayName() != null) {
            existing.setDisplayName(user.getDisplayName());
          }
          if (user.getSkillLevel() != 0) {
            existing.setSkillLevel(user.getSkillLevel());
          }
          return userRepository.save(existing);
        })
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Deletes users.
   *
   * @param id
   * @return user or throw exception.
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id, Authentication auth) {
    User current = (User) auth.getPrincipal();
    if (id != current.getId() && current.getRole() != Role.ADMINISTRATOR) {
      throw new AccessControlException("");
    }
    userRepository.findById(id)
        .map((user) -> {
          userRepository.delete(user);
          return null;

        })
        .orElse(null);

  }

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> get(Authentication auth) {
    User user = (auth != null) ? (User) auth.getPrincipal() : null;
    return ResponseEntity.of(Optional.ofNullable(user));
  }

}