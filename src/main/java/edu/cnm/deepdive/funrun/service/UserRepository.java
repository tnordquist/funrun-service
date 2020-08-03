package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.model.entity.User.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Holds user information as well as being able to discover interfaces that extend this one during
 * classpath scanning for easy Spring bean creation.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Allows an object to be the target of the "for-each loop" statement that extend this one during
   * classpath scanning for easy Spring bean creation.
   */
  Iterable<User> getAllByOrderByDisplayNameAsc();

  Iterable<User> getAllByOrderBySkillLevelAsc();

  Optional<User> findFirstByOauthKey(String oauthKey);

  Iterable<User> getAllByRoleOrderByDisplayNameAsc(Role role);


}

