package edu.cnm.deepdive.funrun.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.net.URI;
import java.util.Date;

/**
 * Shows a non-hierarchical relation, for many to one relationships with event, and one to many
 * relationships with comment.
 */
@JsonPropertyOrder({"id", "start", "end", "user", "event", "distance"})
public interface FlatHistory {

  Long getId();

  Date getStart();

  Date getEnd();

  //User getUser();

 // Event getEvent();

  int getDistance();

  URI getHref();
}
