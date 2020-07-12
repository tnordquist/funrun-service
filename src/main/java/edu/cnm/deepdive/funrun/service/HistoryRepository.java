package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.Event;
import edu.cnm.deepdive.funrun.model.entity.History;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<History, Long> {

  Iterable<History> getAllByOrderByNameAsc();

  Iterable<History> getAllByCreatedBetweenOrderByCreatedDesc(Date start, Date end);

  Iterable<History> getAllByOrderByDistanceAsc();

  Iterable<History> getAllByEventOrderByTextAsc(Event event);



}