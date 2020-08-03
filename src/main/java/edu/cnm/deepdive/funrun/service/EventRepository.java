package edu.cnm.deepdive.funrun.service;


import edu.cnm.deepdive.funrun.model.entity.Event;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Holds event's information as well as being able to discover interfaces that extend this one
 * during classpath scanning for easy Spring bean creation.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

  /**
   * Allows an object to be the target of the "for-each loop" statement that extend this one during
   * classpath scanning for easy Spring bean creation.
   */
  Iterable<Event> getAllByOrderByNameAsc();

  Iterable<Event> getAllByCreatedBetweenOrderByCreatedDesc(Date start, Date end);

  Iterable<Event> getAllByOrderBySkillLevelAsc();

  Iterable<Event> getAllByOrderByDistanceAsc();

  Iterable<Event> getAllByOrderByDisplayNameAsc();

}
