package edu.cnm.deepdive.funrun.service;


import edu.cnm.deepdive.funrun.model.entity.User;
import edu.cnm.deepdive.funrun.model.entity.User.Role;
import java.security.AccessControlException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * provides User manipulation methods exposed for remote API and actions.
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  /**
   * Creates and instance of UserService class.
   *
   * @param userRepository which give the findFirstByOauthKey
   */
  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns an user with a oauthKey and with a displayed name.
   *
   * @param oauthKey    a string oauthKey for the user
   * @param displayName a string displayName for the user
   * @return user saved with oauthKey and displayName
   */
  public synchronized User readOrCreateOne(String oauthKey, String displayName) {
    return userRepository.findFirstByOauthKey(oauthKey)
        .orElseGet(() -> {
          User user = new User();
          user.setOauthKey(oauthKey);
          user.setDisplayName(displayName);
          return userRepository.save(user);
        });
  }

  /**
   * Returns an optional user by it's id.
   *
   * @param id that the user has
   * @return user by id
   */
  public Optional<User> get(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> get(Authentication auth) {
    return Optional.ofNullable((User) auth.getPrincipal());
  }

  /**
   * Returns an exception if the user doesn't provide a valid id and is not the Administrator.
   *
   * @param user   that require access
   * @param userId the id provided by the user
   * @throws AccessControlException
   */
  public void requireAccess(User user, long userId) {
    if (userId != user.getId() && user.getRole() != Role.ADMINISTRATOR) {
      throw new AccessControlException("");
    }
  }

  /**
   * Returns an current user based on its auth.
   *
   * @param auth that the user has
   * @return auth for the user
   */
  public User getCurrentUser(Authentication auth) {
    return (User) auth.getPrincipal();
  }


}
