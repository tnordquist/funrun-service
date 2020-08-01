package edu.cnm.deepdive.funrun.model.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.funrun.view.FlatComment;
import edu.cnm.deepdive.funrun.view.FlatHistory;
import java.net.URI;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;

/**
 * This entity class contains static methods, with convenience annotations,
 * which provides additional information about comments to assist Hibernate in mapping
 * an entity class field to a table column in a Apache Derby database, and retrieved.
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Comment implements FlatComment {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "comment_id", nullable = false, updatable = false)
  private Long id;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date date;

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "history_id")
  @JsonSerialize(as = FlatHistory.class)
  private History history;  //this is a field

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @NonNull
  @Column(length = 1000, nullable = false)
  private String text;

  /**
   * Getters and setters give permission to invoke methods in other classes.
   *
   * @param
   * @return text, id, history, author.
   */
  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public History getHistory() {
    return history;
  }

  public void setHistory(History history) {
    this.history = history;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @PostConstruct
  private void initHateoas() {
    //noinspection ResultOfMethodCallIgnored
    entityLinks.toString();
  }

  @Autowired

  private void setEntityLinks(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") EntityLinks entityLinks) {
    Comment.entityLinks = entityLinks;
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
    return (id != null) ? entityLinks.linkForItemResource(Comment.class, id).toUri() : null;
  }

}
