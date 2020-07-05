package edu.cnm.deepdive.funrun.service;


import edu.cnm.deepdive.funrun.model.entity.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {
  Iterable<Event> getAllByOrderByNameAsc();

  @Query("SELECT * FROM Event ORDER BY skillLevel")
  LiveDate<List<Event>> selectAll();


}
