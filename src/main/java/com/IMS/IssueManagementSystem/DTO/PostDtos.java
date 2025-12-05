package com.IMS.IssueManagementSystem.DTO;

import com.IMS.IssueManagementSystem.Model.enums.PostType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDtos {

    @Data
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private String contact;
    }

    @Data
    @AllArgsConstructor
    public static class PostResponse {
        private Long id;
        private String title;
        private String description;
        private String postType;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UserResponse createdBy;

    }

    @Data
    public static class CreateRequest {
        @NotEmpty(message = "PostType must not be empty.")
        private PostType postType;
        @NotEmpty(message = "Title must not be empty.")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        private String title;
        @NotEmpty(message = "Description must not be empty")
        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        private String description;
    }

    @Data
    public static class UpdateRequest {
        @NotEmpty(message = "PostType must not be empty.")
        private PostType postType;
        @NotEmpty(message = "Title must not be empty")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        private String title;
        @NotEmpty(message = "Description must not be empty")
        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        private String description;
    }

    @Data
    public static class StatusUpdateRequest {
        private String status;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private int code;
        private String message;
        private String status;
        private PostResponse data;
    }

    @Data
    @AllArgsConstructor
    public static class ChangePostStatusRequest {
        @NotNull
        private Long id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostStatisticsDto {
        private int totalActiveUsers;
        private int totalApprovedPosts;
        private int totalRejectedPosts;
        private int totalPendingPosts;
        private int totalClosedPosts;
    }
}
