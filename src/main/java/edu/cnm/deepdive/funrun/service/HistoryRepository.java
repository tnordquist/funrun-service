package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Holds history's information as well as being able to discover interfaces that extend this one
 * during classpath scanning for easy Spring bean creation.
 */
public interface HistoryRepository extends JpaRepository<History, Long> {

  /**
   * Allows an object to be the target of the "for-each loop" statement that extend this one during
   * classpath scanning for easy Spring bean creation.
   */
  Iterable<History> getAllByEventAndStartBetween(Event event, Date start, Date end);

  Iterable<History> getAllByUserAndStartBetween(User user, Date start, Date end);

  Iterable<History> getAllByUserOrderByDistanceAsc(User user);

  Iterable<History> getAllByEventOrderByStartDescEndDesc(Event event);

  Iterable<History> getAllByUserOrderByStartDescEndDesc(User user);


}