package edu.cnm.deepdive.funrun.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;
import org.springframework.lang.NonNull;

@JsonPropertyOrder({"id", "skillLevel", "displayName", "start", "end", "distance"})
public interface FlatEvent {

  Long getId();

  int getSkillLevel();

  @NonNull
  String getDisplayName();

  Date getStart();

  Date getEnd();

  int getDistance();

  URI getHref();

}
