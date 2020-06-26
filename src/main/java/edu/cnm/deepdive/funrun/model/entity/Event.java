package edu.cnm.deepdive.funrun.model.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.lang.NonNull;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "event_id", nullable = false, updatable = false)
  private Long id;

  @NonNull
  @Column(nullable = false, unique = true)
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

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  public Date getStart() {
    return start;
  }

  public Date getEnd() {
    return end;
  }

  public int getDistance() {
    return distance;
  }

}
