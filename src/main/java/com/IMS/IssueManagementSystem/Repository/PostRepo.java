package com.IMS.IssueManagementSystem.Repository;

import com.IMS.IssueManagementSystem.Model.Post;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    // Find a post by its ID
    Optional<Post> findById(Long id);

    // Find posts by their status
    List<Post> findByStatus(PostStatus status);

    // Find posts created by a specific user
    List<Post> findByCreatedById(Long userId);

    // Find posts by title
    List<Post> findByTitleContainingIgnoreCase(String title);

    List<Post> findByCreatedByIdAndStatus(Long targetUserId, PostStatus postStatus);
}
