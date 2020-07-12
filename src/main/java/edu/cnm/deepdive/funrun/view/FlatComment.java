package edu.cnm.deepdive.funrun.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;

@JsonPropertyOrder({"id", "created", "updated", "text"})
public interface FlatComment {

  Long getId();

  Date getCreated();

  Date getUpdated();

  String getText();

  URI getHref();
}
