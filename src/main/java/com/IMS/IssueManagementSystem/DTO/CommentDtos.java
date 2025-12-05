package com.IMS.IssueManagementSystem.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDtos {
    @Data
    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String contact;

        // Constructor for UserResponse
        public UserResponse(Long id, String username, String email, String contact) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.contact = contact;
        }
    }

    @Data
    public static class CreateCommentRequest {
        @NotEmpty(message = "postId must not be empty")
        private Long postId;
        @NotEmpty(message = "Comment text must not be empty")
        private String content;
    }

    @Data
    public static class UpdateCommentRequest {
        @NotEmpty(message = "Comment text must not be empty")
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class CommentResponse {
        private Long id;
        private String content;
        private Long postId;
        private UserResponse createdBy;
        private LocalDateTime createdAt;
    }

    @AllArgsConstructor
    @Data
    public static class Response {
        private int code;
        private String message;
        private String status;
        private LocalDateTime timestamp;
        private CommentResponse commentResponse;  // Single comment response
        private List<CommentResponse> commentResponses;  // List of comment responses

        // Constructor for single comment response
        public Response(int code, String message, String status, LocalDateTime timestamp, CommentResponse commentResponse) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
            this.commentResponse = commentResponse;
            this.commentResponses = null;  // Null because we're returning a single comment
        }

        // Constructor for list of comment responses
        public Response(int code, String message, String status, LocalDateTime timestamp, List<CommentResponse> commentResponses) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
            this.commentResponse = null;  // Null because we're returning a list of comments
            this.commentResponses = commentResponses;
        }
        public Response(int code, String message, String status, LocalDateTime timestamp) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
        }
    }

}
