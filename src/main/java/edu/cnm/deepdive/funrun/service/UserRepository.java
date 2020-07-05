package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository <User, Long> {

  Iterable<User> getAllByOrderByNameAsc();

  @Query("SELECT * FROM User ORDER BY displayName")
  LiveData<List<User>> selectAll();


}

