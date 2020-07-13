package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.service.CommentRepository;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
import edu.cnm.deepdive.funrun.service.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
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
@RequestMapping("/history")
@ExposesResourceFor(History.class)
public class HistoryController {

  private final HistoryRepository historyRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final EventRepository eventRepository;


  @Autowired
  public HistoryController(HistoryRepository historyRepository,
      UserRepository userRepository, CommentRepository commentRepository,
      EventRepository eventRepository) {
    this.historyRepository = historyRepository;
    this.userRepository = userRepository;
    this.commentRepository = commentRepository;
    this.eventRepository = eventRepository;
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

/*  @PutMapping(value = "/{id:\\d+}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public History put(@PathVariable long id, @RequestBody History history) {
   *//* History existingHistory = get(id);  //it is a history that is in database
    if (history.getStart() != null) {
      existingHistory.setStart(history.getStart()); //Added getStart as a date; not sure need to check with the team
    }
    return historyRepository.save(existingHistory); *//*//save back to database the modified source
  }*/

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)  //after delete nothing to be returned
  public void delete(@PathVariable long id) {
    historyRepository.findById(id)
        /*.map((history) -> {
          *//*List<History> histories = history.getHistories();
          histories.forEach((quote) -> quote.setHistory(null));*//*//go to each quotes and set the source to null
          historyRepository.saveAll(histories);
          return history;
        })*/ //TODO Add to HistoryEntity the list of Histories.
        .map((history) -> {
          historyRepository.delete(history);
          return history;
        })
        .orElseThrow(NoSuchElementException::new);

  }
}

