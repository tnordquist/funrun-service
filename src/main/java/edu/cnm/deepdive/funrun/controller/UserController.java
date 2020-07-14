package edu.cnm.deepdive.funrun.controller;


import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import edu.cnm.deepdive.funrun.service.UserService;
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

@RestController
@RequestMapping("/users")
@ExposesResourceFor(User.class)
public class UserController {

  private final UserRepository userRepository;
  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;


  public UserController(UserRepository userRepository,
      HistoryRepository historyRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<User> get() {
    return userRepository.getAllByOrderByNameAsc();
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get(@PathVariable long id) {
    return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public User put(@PathVariable long id, @RequestBody User user) {
    User existingUser = get(id);
    if (user.getDisplayName() != null) {
      existingUser.setDisplayName(user.getDisplayName());
    }
    if (user.getSkillLevel() != 0) {
      existingUser.setSkillLevel(user.getSkillLevel());
    }
    return userRepository.save(existingUser);
  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    userRepository.findById(id)
        .map((user) -> {
          List<Comment> comments = user.getComments();
          comments.forEach((comment) -> comment.setAuthor(null));
          commentRepository.saveAll(comments);
          return user;
        })
        .map((user) -> {
          userRepository.delete(user);
          return user;

        })
        .orElseThrow(NoSuchElementException::new);

    userRepository.findById(id)
        .map((user) -> {
          List<History> histories = user.getHistories();
          histories.forEach((history) -> history.setStart(null));
          historyRepository.saveAll(histories);
          return user;
        })
        .map((user) -> {
          userRepository.delete(user);
          return user;

        })
        .orElseThrow(NoSuchElementException::new);
  }

}