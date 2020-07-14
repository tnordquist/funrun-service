package edu.cnm.deepdive.funrun.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.funrun.view.FlatComment;
import edu.cnm.deepdive.funrun.view.FlatHistory;
import edu.cnm.deepdive.funrun.view.FlatUser;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "user_profile")
@Component
@JsonIgnoreProperties(
    value = {"id", "displayName", "skillLevel", "href", "oauthKey"},
    allowGetters = true,
    ignoreUnknown = true
)
public class User implements FlatUser {

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

  @OneToMany(                             //given name of the field.JPA annotation
      fetch = FetchType.LAZY,
      mappedBy = "user",
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
  )
  @OrderBy("start DESC")
  @JsonSerialize(contentAs = FlatHistory.class)
  private List<History> histories = new LinkedList<>();

  @OneToMany
  @OrderBy("text ASC")
  @JsonSerialize(contentAs = FlatComment.class)
  private List<Comment> comments = new LinkedList<>();

  @Enumerated(value = EnumType.ORDINAL)
  @Column(nullable = false)
  private Role role = Role.USER;


  public Long getId() {
    return id;
  }

  @Override
  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;
  }


  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
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

  public List<History> getHistories() {
    return histories;
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
    User.entityLinks = entityLinks;
  }

  @Override
  public URI getHref() {
    return (id != null) ? entityLinks.linkForItemResource(User.class, id).toUri() : null;
  }

  public enum Role {
    USER, ADMINISTRATOR
  }
}
