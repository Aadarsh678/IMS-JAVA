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

        @NotEmpty(message = "Username must not be empty")
        @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        private String userName;

        @NotEmpty(message = "Password must not be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        private String password;

        @NotEmpty(message = "Email must not be empty")
        @Email(message = "Email must be valid")
        private String email;

        @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
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
        @NotEmpty(message = "Email must not be empty")
        @Email(message = "Email must be valid")
        private String email;

        @NotEmpty(message = "Password must not be empty")
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
