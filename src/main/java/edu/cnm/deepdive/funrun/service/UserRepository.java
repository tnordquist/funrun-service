package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository <User, Long> {

  Iterable<User> getAllByOrderByNameAsc();

  Iterable<User> getAllByOrderByDisplayNameAsc();

  Iterable<User>  getAllByOrderBySkillLevelAsc();


}

