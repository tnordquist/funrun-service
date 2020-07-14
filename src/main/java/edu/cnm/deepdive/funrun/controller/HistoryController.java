package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
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
@RequestMapping("/histories")
@ExposesResourceFor(History.class)
public class HistoryController {

  private final HistoryRepository historyRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;


  @Autowired
  public HistoryController(HistoryRepository historyRepository,
      UserRepository userRepository, CommentRepository commentRepository) {
    this.historyRepository = historyRepository;
    this.userRepository = userRepository;
    this.commentRepository = commentRepository;

  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<User> get() {
    return userRepository.getAllByOrderByNameAsc();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<History> post(@RequestBody History history) {
    historyRepository.save(history);
    return ResponseEntity.created(history.getHref()).body(history);
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public History get(@PathVariable long id) {
    return historyRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @PutMapping(value = "/{id:\\d+}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public History put(@PathVariable long id, @RequestBody History history) {
    History existingHistory = get(id);
    if (history.getStart() != null) {
      existingHistory.setStart(
          history.getStart());
    }
    return historyRepository.save(existingHistory);
  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    historyRepository.findById(id)
        .map((history) -> {
          List<Comment> comments = history.getComments();
          comments.forEach(
              (comment) -> comment.setHistory(null));
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

