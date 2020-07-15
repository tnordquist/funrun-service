package edu.cnm.deepdive.funrun.service;


import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface holds event information as well as being able to discover interfaces
 * that extend this one during classpath scanning for easy Spring bean creation.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

  /**
   * Implementing this interface allows an object to be the target of the "for-each loop" statement
   * that extend this one during classpath scanning for easy Spring bean creation.
   */
  Iterable<Event> getAllByOrderByNameAsc();

  Iterable<Event> getAllByCreatedBetweenOrderByCreatedDesc(Date start, Date end);

  Iterable<Event>  getAllByOrderBySkillLevelAsc();

  Iterable<Event> getAllByOrderByDistanceAsc();

  Iterable<Event> getAllByOrderByDisplayNameAsc();

}
