package com.IMS.IssueManagementSystem.Repository;

import com.IMS.IssueManagementSystem.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    Comment findById(long id);
    Comment findByuserId(long id);
    Comment findBypostId(long id);
}
