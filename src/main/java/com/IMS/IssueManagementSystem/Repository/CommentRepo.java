package com.IMS.IssueManagementSystem.Repository;

import com.IMS.IssueManagementSystem.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    Comment findById(long id);
//    Comment findByuserIdpostId(long userId,long postId);
    List<Comment> findByPostId(long id);
}
