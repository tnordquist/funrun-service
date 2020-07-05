package edu.cnm.deepdive.funrun.service;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Event extends JpaRepository<Event, Long> {
  Iterable<Event> getAllByOrderByNameAsc();

  @Query("SELECT * FROM Event ORDER BY skillLevel")
  LiveDate<List<Event>> selectAll();


}
