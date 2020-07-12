package edu.cnm.deepdive.funrun.service;


import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

  Iterable<Event> getAllByOrderByNameAsc();

  Iterable<Event> getAllByCreatedBetweenOrderByCreatedDesc(Date start, Date end);

  Iterable<Event>  getAllByOrderBySkillLevelAsc();

  Iterable<Event> getAllByOrderByDistanceAsc();

  Iterable<Event> getAllByOrderByDisplayNameAsc();

}
