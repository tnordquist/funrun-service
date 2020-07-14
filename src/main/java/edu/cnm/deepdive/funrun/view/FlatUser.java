package edu.cnm.deepdive.funrun.view;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;
import org.springframework.lang.NonNull;


@JsonPropertyOrder({"id", "displayName", "oauthKey", "skillLevel"})
public interface FlatUser {

  Long getId();

  @NonNull
  String getDisplayName();

  Date getOauthKey();

  Date getSkillLevel();

  URI getHref();
}
