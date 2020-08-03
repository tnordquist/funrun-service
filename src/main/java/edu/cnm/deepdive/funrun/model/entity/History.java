package edu.cnm.deepdive.funrun.model.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.funrun.view.FlatComment;
import edu.cnm.deepdive.funrun.view.FlatHistory;
import edu.cnm.deepdive.funrun.view.FlatUser;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Contains static methods, with convenience annotations, which provides additional information
 * about history to assist Hibernate in mapping an entity class field to a table column in a Apache
 * Derby database, and retrieved.
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Component
public class History implements FlatHistory {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "history_id", nullable = false, updatable = false)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_of_run", nullable = false, updatable = false)
  private Date start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end_of_run", nullable = false)
  private Date end;

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "user_id", nullable = false)
  @JsonSerialize(as = FlatUser.class)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "event_id", nullable = true)
  private Event event;

  @NonNull
  @Column(length = 4096, nullable = false)
  private int distance;

  @OneToMany(                             //given name of the field.JPA annotation
      fetch = FetchType.LAZY,
      mappedBy = "history",
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE},
      orphanRemoval = true
  )
  @OrderBy("date DESC")
  @JsonSerialize(contentAs = FlatComment.class)
  private List<Comment> comments = new LinkedList<>();


  /**
   * Returns history's id
   *
   * @return a long containing the value for Id.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns history's start date.
   *
   * @return a start date of the history.
   */
  public Date getStart() {
    return start;
  }

  /**
   * Sets the history's start date.
   */
  public void setStart(Date start) {
    this.start = start;
  }

  /**
   * Returns history's end date.
   *
   * @return the end date for history.
   */
  public Date getEnd() {
    return end;
  }

  /**
   * Sets the history's end date.
   */
  public void setEnd(Date end) {
    this.end = end;
  }

  /**
   * Returns the user for that specific history.
   *
   * @return the user.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the history's user.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the event for that specific history.
   *
   * @return the event.
   */
  public Event getEvent() {
    return event;
  }

  /**
   * Sets the event for the specific history.
   */
  public void setEvent(Event event) {
    this.event = event;
  }

  /**
   * Returns the distance for that specific history.
   *
   * @return an int containing the value for the distance.
   */
  public int getDistance() {
    return distance;
  }

  /**
   * Sets the distance in the specific history.
   */
  public void setDistance(int distance) {
    this.distance = distance;

  }

  /**
   * Returns a list of comments for the specific history.
   *
   * @return comments for specific history.
   */
  public List<Comment> getComments() {
    return comments;
  }

  @PostConstruct
  private void initHateoas() {
    //noinspection ResultOfMethodCallIgnored
    entityLinks.toString();
  }

  @Autowired
  private void setEntityLinks(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") EntityLinks entityLinks) {
    History.entityLinks = entityLinks;
  }

  /**
   * Defines a link between the current element and the destination anchor defined by this
   * attribute.
   *
   * @return entityLinks if id is not null, or null
   */
  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(History.class, id).toUri() : null;
  }

}

