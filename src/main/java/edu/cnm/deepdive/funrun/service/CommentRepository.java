package edu.cnm.deepdive.funrun.service;

import edu.cnm.deepdive.funrun.model.entity.Comment;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {


  Iterable<Comment> getAllByCreatedBetweenOrderByCreatedAsc(Date start, Date end);

  Iterable<Comment> getAllByTextContainingOrderByTextAsc(String filter);

  Iterable<Comment> getAllByOrderByAuthorAsc();

}
