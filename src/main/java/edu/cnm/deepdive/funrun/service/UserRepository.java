package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.model.entity.User.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository <User, Long> {

  Iterable<User> getAllByOrderByNameAsc();

  Iterable<User> getAllByOrderByDisplayNameAsc();

  Iterable<User>  getAllByOrderBySkillLevelAsc();

  Optional<User> findFirstByOauthKey(String oauthKey);

  Iterable<User> getAllByRoleOrderByDisplayNameAsc(Role role);


}

