package edu.cnm.deepdive.funrun.view;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;
import org.springframework.lang.NonNull;


@JsonPropertyOrder({"id", "displayName", "skillLevel"})
public interface FlatUser {

  Long getId();

  @NonNull
  String getDisplayName();

  int getSkillLevel();

  URI getHref();
}
