package com.IMS.IssueManagementSystem.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

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
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UserResponse createdBy;

    }

    @Data
    public static class CreateRequest {
        private String title;
        private String description;
    }

    @Data
    public static class UpdateRequest {
        private String title;
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
}
