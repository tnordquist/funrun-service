package edu.cnm.deepdive.funrun.view;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import org.springframework.lang.NonNull;

/**
 * Shows a non-hierarchical relation, for one to many relationships with history and comment.
 */
@JsonPropertyOrder({"id", "displayName", "skillLevel", "Href", "role"})
public interface FlatUser {

  Long getId();

  @NonNull
  String getDisplayName();

  int getSkillLevel();

  URI getHref();
}
