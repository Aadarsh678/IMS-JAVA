package com.IMS.IssueManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentDtos {
    @Data
    public static class CreateRequest {
        private Long postId;
        private String text;
    }

    @Data
    public static class UpdateRequest {
        private String text;
    }

    @AllArgsConstructor
    @Data
    public static class CommentResponse {
        private Long id;
        private String text;
        private Long postId;
        private String createdBy;
        private LocalDateTime createdAt;
    }

    @AllArgsConstructor
    @Data
    public static class Response{
        private int code;
        private String message;
        private String status;
        private LocalDateTime timestamp;
        private CommentResponse commentResponse;
    }
}
