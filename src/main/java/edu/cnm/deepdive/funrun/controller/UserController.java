package edu.cnm.deepdive.funrun.controller;


import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myfunrun")
@ExposesResourceFor(User.class)
public class UserController {

  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;

  @Autowired
  public UserController(UserRepository userRepository,
      EventRepository eventRepository,
      HistoryRepository historyRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
    this.historyRepository = historyRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<User> get() {
    return userRepository.getAllByOrderByNameAsc();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> post(@RequestBody User user) {
    userRepository.save(user);
    // return ResponseEntity.created(user.getHref()).body(user);
    return null; // TODO Add getHref in user entity.
  }
}