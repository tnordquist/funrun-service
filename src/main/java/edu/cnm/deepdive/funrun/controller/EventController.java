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

@RestController
@RequestMapping("/events")
@ExposesResourceFor(Event.class)
public class EventController {


  private final EventRepository eventRepository;
  private final HistoryRepository historyRepository;


  @Autowired
  public EventController(
      EventRepository eventRepository,
      HistoryRepository historyRepository) {

    this.eventRepository = eventRepository;
    this.historyRepository = historyRepository;

  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Event> get() {
    return eventRepository.getAllByOrderByNameAsc();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Event> post(@RequestBody Event event) {
    eventRepository.save(event);
    return ResponseEntity.created(event.getHref()).body(event);
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Event get(@PathVariable long id) {
    return eventRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @PutMapping(value = "/{id:\\d+})",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Event put(@PathVariable long id, @RequestBody Event event) {
    Event existingEvent = get(id);
    if (event.getDisplayName() != null) {
      existingEvent.setDisplayName(event.getDisplayName());
    }
    return eventRepository.save(existingEvent);

  }

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

