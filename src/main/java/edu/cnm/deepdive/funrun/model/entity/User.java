package edu.cnm.deepdive.funrun.model.entity;


import java.net.URI;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class User { // TODO Add implement Flat class.

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id", nullable = false, updatable = false)
  private Long id;

  @NonNull
  @Column(length = 100, nullable = false, unique = true)
  private String displayName;

  @NonNull
  @Column(length = 100, nullable = false, unique = true)
  private String oauthKey;

  @NonNull
  @Column(nullable = false)
  private int skillLevel;

  public Long getId() {
    return id;
  }

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;
  }

  @NonNull
  public String getOauthKey() {
    return oauthKey;
  }

  public void setOauthKey(@NonNull String oauthKey) {
    this.oauthKey = oauthKey;
  }

  public int getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(int skillLevel) {
    this.skillLevel = skillLevel;
  }

  @PostConstruct
  private void initHateoas() {
    //noinspection ResultOfMethodCallIgnored
    entityLinks.toString();
  }

  @Autowired

  private void setEntityLinks(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") EntityLinks entityLinks) {
    User.entityLinks = entityLinks;
  }

  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(User.class, id).toUri() : null;
  }



}
