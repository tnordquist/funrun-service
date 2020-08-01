package edu.cnm.deepdive.funrun.model.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.funrun.view.FlatComment;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Source;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;

/**
 * This entity class contains static methods, with convenience annotations,
 * which provides additional information about history to assist Hibernate in mapping
 * an entity class field to a table column in a Apache Derby database, and retrieved.
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class History implements FlatHistory {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "history_id", nullable = false, updatable = false)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date end;

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "user_id", nullable = false)
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
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
      orphanRemoval = true
  )
  @OrderBy("date DESC")
  @JsonSerialize(contentAs = FlatComment.class)
  private List<Comment> comments = new LinkedList<>();


  /**
   * Getters and setters give permission to invoke methods in other classes.
   *
   * @param
   * @return text, id, start, end, user, event, distance, comments.
   */

  public Long getId() {
    return id;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;

  }

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
   * URI for linked resource. Href defines a link between the current element
   * and the destination anchor defined by this attribute
   *
   * @param
   * @return entityLinks if id is not null, or else null
   */
  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(History.class, id).toUri() : null;
  }

}

