package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.Event;
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
@RequestMapping ("/myfunrun")
@ExposesResourceFor(Event.class)
public class EventController {

  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final HistoryRepository historyRepository;
  private final CommentRepository commentRepository;

  @Autowired
  public EventController(UserRepository userRepository,
      EventRepository eventRepository,
      HistoryRepository historyRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
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
    // return ResponseEntity.created(user.getHref()).body(user);
    return null; // TODO Add getHref in user entity.
  }

  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Event put(@PathVariable long id, @RequestBody Event event) {
    Event existingEvent = get(event);
    if (event.getDisplayName() != null) {
      existingEvent.setDisplayName(event.getDisplayName());
    }
    return eventRepository.save(existingEvent);

  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    eventRepository.findById(id)
/*        .map((source) -> {
          List<Quote> quotes = source.getQuotes();
          quotes.forEach((quote) -> quote.setSource(null));
          quoteRepository.saveAll(quotes);
          return source;
        })
        .map((source) -> {
          sourceRepository.delete(source);
          return source;

        })
        .orElseThrow(NoSuchElementException::new);
  }

*/
  }
}
