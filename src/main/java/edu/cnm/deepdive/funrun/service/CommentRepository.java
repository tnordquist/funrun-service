package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import edu.cnm.deepdive.funrun.model.entity.History;
import edu.cnm.deepdive.funrun.model.entity.User;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Holds comment's information as well as being able to discover interfaces that extend this one
 * during classpath scanning for easy Spring bean creation.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * Implements this interface allows an object to be the target of the "for-each loop" statement
   * that extend this one during classpath scanning for easy Spring bean creation.
   */
  Iterable<Comment> getAllByDateBetweenOrderByDateAsc(Date start, Date end);

  Iterable<Comment> getAllByTextContainingOrderByTextAsc(String filter);

  Iterable<Comment> getAllByHistoryOrderByDateDesc(History history);

  Iterable<Comment> getAllByAuthorOrderByDateDesc(User author);

}
