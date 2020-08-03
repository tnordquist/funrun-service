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
 * Contains static methods, with convenience annotations, which provides additional information
 * about comments to assist Hibernate in mapping an entity class field to a table column in a Apache
 * Derby database, and retrieved.
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
  private History history;

  @ManyToOne(fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @NonNull
  @Column(length = 1000, nullable = false)
  private String text;

  /**
   * Returns comment's text
   *
   * @return a string containing the comment's text.
   */
  @NonNull
  public String getText() {
    return text;
  }


  /**
   * Sets the comment's text.
   */
  public void setText(@NonNull String text) {
    this.text = text;
  }

  /**
   * Returns comment's id
   *
   * @return a long containing the value for Id.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns comment's date
   *
   * @return a Date containing the value for date.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets the comment's date.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Retrieves the history.
   *
   * @return history.
   */
  public History getHistory() {
    return history;
  }

  /**
   * Sets the history.
   */
  public void setHistory(History history) {
    this.history = history;
  }

  /**
   * Retrieves the Author of the comment.
   *
   * @return author.
   */
  public User getAuthor() {
    return author;
  }

  /**
   * Sets the author.
   */
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
   * Defines a link between the current element and the destination anchor defined by this
   * attribute
   *
   * @return entityLinks if id is not null, or else null
   */
  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(Comment.class, id).toUri() : null;
  }

}
