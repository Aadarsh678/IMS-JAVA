package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.DTO.CommentDtos;
import com.IMS.IssueManagementSystem.Model.Comment;
import com.IMS.IssueManagementSystem.Model.Post;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Repository.CommentRepo;
import com.IMS.IssueManagementSystem.Repository.PostRepo;
import com.IMS.IssueManagementSystem.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    // Helper method to convert Comment to CommentResponse DTO
    private CommentDtos.CommentResponse convertToCommentResponse(Comment comment) {
        User user = comment.getCreatedBy();

        // Create UserResponse for the createdBy field
        CommentDtos.UserResponse userResponse = new CommentDtos.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getContact()
        );

        return new CommentDtos.CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getPost().getId(),
                userResponse,  // Use the UserResponse
                comment.getCreatedDate()
        );
    }


    // Create a new comment
    public CommentDtos.Response createComment(CommentDtos.CreateCommentRequest createRequest, Long userId) {
        Post post = postRepo.findById(createRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setText(createRequest.getContent());
        comment.setPost(post);
        comment.setCreatedBy(user);
        comment.setCreatedDate(LocalDateTime.now());

        Comment savedComment = commentRepo.save(comment);
        CommentDtos.CommentResponse commentResponse = convertToCommentResponse(savedComment);

        return new CommentDtos.Response(200, "Comment created successfully", "SUCCESS",LocalDateTime.now(), commentResponse);
    }

    // Update an existing comment
    public CommentDtos.Response updateComment(Long commentId, CommentDtos.UpdateCommentRequest updateRequest,Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if(!comment.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Cannot delete another users comment");
        }

        comment.setText(updateRequest.getContent());
        comment.setUpdatedDate(LocalDateTime.now());

        Comment updatedComment = commentRepo.save(comment);
        CommentDtos.CommentResponse commentResponse = convertToCommentResponse(updatedComment);

        return new CommentDtos.Response(200, "Comment updated successfully", "SUCCESS",LocalDateTime.now(), commentResponse);
    }

    // Get a comment by ID
    public CommentDtos.Response getCommentById(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        CommentDtos.CommentResponse commentResponse = convertToCommentResponse(comment);
        return new CommentDtos.Response(200, "Comment retrieved successfully", "SUCCESS",LocalDateTime.now(), commentResponse);
    }

    // Delete a comment (soft delete by marking it as inactive)
    public CommentDtos.Response deleteComment(Long commentId,Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this comment.");
        }
        commentRepo.delete(comment);

        return new CommentDtos.Response(200, "Comment deleted successfully", "SUCCESS",LocalDateTime.now());
    }

    public CommentDtos.Response getAllCommentsByPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Fetch all comments related to the post
        List<Comment> comments = commentRepo.findByPostId(postId);  // Use the correct method name
        List<CommentDtos.CommentResponse> commentResponses = new ArrayList<>();

        // Convert each comment to CommentResponse DTO
        for (Comment comment : comments) {
            commentResponses.add(convertToCommentResponse(comment));  // Use the helper method
        }

        // Return the response with the list of comments
        return new CommentDtos.Response(200, "List of comments by Post", "SUCCESS", LocalDateTime.now(), commentResponses);
    }
}
