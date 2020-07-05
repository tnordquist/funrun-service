package edu.cnm.deepdive.funrun.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface History extends JpaRepository<History, Long> {
  Iterable<History> getAllByOrderByNameAsc();

  @Query("SELECT * FROM History ORDER BY event")
  LiveData<List<History>> selectAll();

}
