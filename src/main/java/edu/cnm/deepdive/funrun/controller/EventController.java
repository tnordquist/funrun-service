package edu.cnm.deepdive.funrun.controller;

import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.service.EventRepository;
import edu.cnm.deepdive.funrun.service.HistoryRepository;
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
 * Indicates that Spring Boot controller class Event is able to respond to HTTP requests.
 */
@RestController
@RequestMapping("/events")
@ExposesResourceFor(Event.class)
public class EventController {


  private final EventRepository eventRepository;
  private final HistoryRepository historyRepository;

  /**
   * Creates a new instance of event class.
   *
   * @param eventRepository
   * @param historyRepository
   */
  @Autowired
  public EventController(
      EventRepository eventRepository,
      HistoryRepository historyRepository) {

    this.eventRepository = eventRepository;
    this.historyRepository = historyRepository;

  }

  /**
   * Retrieves a collection of events that can be iterated based on name.
   *
   * @return all events ordered by name ascending.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Event> get() {
    return eventRepository.getAllByOrderByNameAsc();
  }

  /**
   * Posts events with the corresponding authentication.
   *
   * @param event that is posted
   * @param auth  for the user that add the event
   * @return the new event.
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Event> post(@RequestBody Event event, Authentication auth) {
    eventRepository.save(event);
    return ResponseEntity.created(event.getHref()).body(event);
  }

  /**
   * Retrieves events by their id.
   *
   * @param id that an event has
   * @return events findById
   * @throws NoSuchElementException
   */
  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Event get(@PathVariable long id) {
    return eventRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Allows events to be updated.
   *
   * @param id    for the event to be updated
   * @param event that is updated
   * @param auth  user's authentication
   * @return existing, updated event
   */
  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Event put(@PathVariable long id, @RequestBody Event event, Authentication auth) {
    Event existingEvent = get(id);
    if (event.getDisplayName() != null) {
      existingEvent.setDisplayName(event.getDisplayName());
    }
    return eventRepository.save(existingEvent);

  }

  /**
   * Allows events to be deleted.
   *
   * @param id   for the event to be deleted
   * @param auth user's authentication
   * @return null
   * @throws .orElse null
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id, Authentication auth) {
    eventRepository.findById(id)
        .map((event) -> {
          eventRepository.delete(event);
          return null;

        })
        .orElse(null);
  }

}

