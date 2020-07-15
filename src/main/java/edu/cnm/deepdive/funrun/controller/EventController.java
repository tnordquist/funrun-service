package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
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

/**
 * This class contains static methods, with convenience annotations,
 * which control the race events posted by users. Events may be posted, deleted, and retrieved.
 */

@RestController
@RequestMapping("/events")
@ExposesResourceFor(Event.class)
public class EventController {


  private final EventRepository eventRepository;
  private final HistoryRepository historyRepository;

  /**
   * Allows comments to be posted, retrieved, updated, and deleted.
   *
   * @param eventRepository, historyRepository
   * @return events.
   */
  @Autowired
  public EventController(
      EventRepository eventRepository,
      HistoryRepository historyRepository) {

    this.eventRepository = eventRepository;
    this.historyRepository = historyRepository;

  }

  /**
   * Allows events to be retrieved.
   *
   * @param
   * @return events ordered by name ascending.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Event> get() {
    return eventRepository.getAllByOrderByNameAsc();
  }

  /**
   * Allows events to be posted.
   *
   * @param event
   * @return new event.
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Event> post(@RequestBody Event event) {
    eventRepository.save(event);
    return ResponseEntity.created(event.getHref()).body(event);
  }

  /**
   * Allows events to be retrieved.
   *
   * @param id
   * @return events findById or throw exception.
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Event get(@PathVariable long id) {
    return eventRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows events to be updated.
   *
   * @param id, event
   * @return existing, updated events.
   */
  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Event put(@PathVariable long id, @RequestBody Event event) {
    Event existingEvent = get(id);
    if (event.getDisplayName() != null) {
      existingEvent.setDisplayName(event.getDisplayName());
    }
    return eventRepository.save(existingEvent);

  }
  /**
   * Allows events to be deleted.
   *
   * @param id, event
   * @return event or exception.
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    eventRepository.findById(id)
        .map((event) -> {
          List<History> histories = event.getHistories();
          histories.forEach((history) -> history.setEvent(null));
          historyRepository.saveAll(histories);
          return event;
        })
        .map((event) -> {
          eventRepository.delete(event);
          return event;

        })
        .orElseThrow(NoSuchElementException::new);
  }

}

