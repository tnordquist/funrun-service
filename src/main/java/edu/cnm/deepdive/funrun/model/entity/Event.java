package edu.cnm.deepdive.funrun.model.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.funrun.view.FlatEvent;
import edu.cnm.deepdive.funrun.view.FlatHistory;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;

/**
 * Contains static methods, with convenience annotations, which provides additional information
 * about event to assist Hibernate in mapping an entity class field to a table column in a Apache
 * Derby database, and retrieved.
 */
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


  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "event",
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
      orphanRemoval = true
  )
  @OrderBy("start DESC")
  @JsonSerialize(contentAs = FlatHistory.class)
  private List<History> histories = new LinkedList<>();


  /**
   * Returns event's id
   *
   * @return a long containing the value for Id.
   */
  public Long getId() {
    return id;
  }


  public int getSkillLevel() {
    return skillLevel;
  }

  /**
   * Sets the event's skill level.
   */
  public void setSkillLevel(int skillLevel) {
    this.skillLevel = skillLevel;
  }

  /**
   * Retrieves the display name for the user in the specific event.
   *
   * @return display name as a String.
   */
  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the display name for the user in the specific event.
   */
  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;
  }

  /**
   * Retrieves event's start date.
   *
   * @return a start date of the event.
   */
  public Date getStart() {
    return start;
  }

  /**
   * Sets the event's start date.
   */
  public void setStart(Date start) {
    this.start = start;
  }

  /**
   * Returns event's end date.
   *
   * @return the end date for event.
   */
  public Date getEnd() {
    return end;
  }

  /**
   * Sets the event's end date.
   */
  public void setEnd(Date end) {
    this.end = end;
  }

  /**
   * Returns the distance for specific event.
   *
   * @return distance as an int value.
   */
  public int getDistance() {
    return distance;
  }

  /**
   * Sets the distance in the specific event.
   */
  public void setDistance(int distance) {
    this.distance = distance;
  }

  /**
   * Retrieves a list of histories for specific event.
   *
   * @return histories
   */
  public List<History> getHistories() {
    return histories;
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

  /**
   * Defines a link between the current element and the destination anchor defined by this
   * attribute.
   *
   * @return entityLinks if id is not null, or else null
   */
  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(Event.class, id).toUri() : null;
  }

}

