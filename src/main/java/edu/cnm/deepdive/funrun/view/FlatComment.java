package edu.cnm.deepdive.funrun.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.net.URI;
import java.util.Date;

@JsonPropertyOrder({"id", "history", "author", "text"})
public interface FlatComment {

  Long getId();

  History getHistory();

  User getAuthor();

  String getText();

  URI getHref();
}
