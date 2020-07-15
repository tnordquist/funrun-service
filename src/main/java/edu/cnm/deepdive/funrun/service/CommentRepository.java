package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface holds comment information as well as being able to discover interfaces
 * that extend this one during classpath scanning for easy Spring bean creation.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * Implementing this interface allows an object to be the target of the "for-each loop" statement
   * that extend this one during classpath scanning for easy Spring bean creation.
   */
  Iterable<Comment> getAllByCreatedBetweenOrderByCreatedAsc(Date start, Date end);

  Iterable<Comment> getAllByTextContainingOrderByTextAsc(String filter);

  Iterable<Comment> getAllByOrderByAuthorAsc();

}
