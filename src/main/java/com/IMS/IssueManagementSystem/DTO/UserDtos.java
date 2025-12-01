package com.IMS.IssueManagementSystem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDtos {

    // Register request DTO
    @Data
    public static class RegisterRequest {
        private String userName;
        private String password;
        private String email;
        private String contact;
    }

    // Register response DTO
    @Data
    @AllArgsConstructor
    public static class RegisterResponse {
        private int code;
        private String message;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date timestamp;

        private Data data;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class Data {
            private Long id;
            private String userName;
            private String email;
            private List<String> roleNames;
            private String contact;
        }
    }

    // Login request DTO
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    // Login response DTO
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private int code;
        private String message;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date timestamp;

        private Data data;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class Data {
            private Long id;
            private String userName;
            private String email;
            private List<String> roleNames;
            private String contact;
        }
    }

    @Data
    @AllArgsConstructor
    @Setter
    @Getter
    public static class UserListResponse {
        private Long id;
        private String userName;
        private String email;
        private List<String> roleNames;
        private String contact;
    }

    @Data
    @AllArgsConstructor
    public static class PromoteResponse {
        private int code;
        private String message;
        private String status;
        private LocalDateTime timestamp;
    }

    @Data
    @AllArgsConstructor
    public static class ChangeUserRoleRequest {
        @NotNull
        private Long id;
    }
}
