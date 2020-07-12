package edu.cnm.deepdive.funrun.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;
import org.springframework.lang.NonNull;

@JsonPropertyOrder({"id", "created", "updated", "name"})
public interface FlatEvent {

  Long getId();

  @NonNull
  String getName();

  Date getCreated();

  Date getUpdated();

  URI getHref();

}
