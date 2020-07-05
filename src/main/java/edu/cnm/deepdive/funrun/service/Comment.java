package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Comment extends JpaRepository<Comment, Long> {

  Iterable<Comment> getAllByOrderByNameAsc();

  @Query("SELECT * FROM Comment ORDER BY history")
  LiveData<List<Comment>> selectAll();
}
