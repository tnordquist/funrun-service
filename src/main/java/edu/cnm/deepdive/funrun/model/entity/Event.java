package edu.cnm.deepdive.funrun.model.entity;


import edu.cnm.deepdive.funrun.view.FlatEvent;
import java.net.URI;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Event implements FlatEvent {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "event_id", nullable = false, updatable = false)
  private Long id;

  @NonNull
  @Column(nullable = false)
  private int skillLevel;

  @NonNull
  @Column(length = 100, nullable = false, unique = true)
  private String displayName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = false)
  private Date start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true)
  private Date end;

  @NonNull
  @Column(length = 4096, nullable = false)
  private int distance;

  public Long getId() {
    return id;
  }

  public int getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(int skillLevel) {
    this.skillLevel = skillLevel;
  }

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  @PostConstruct
  private void initHateoas() {
    //noinspection ResultOfMethodCallIgnored
    entityLinks.toString();
  }

  @Autowired

  private void setEntityLinks(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") EntityLinks entityLinks) {
    Event.entityLinks = entityLinks;
  }

  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(Event.class, id).toUri() : null;
  }

}

