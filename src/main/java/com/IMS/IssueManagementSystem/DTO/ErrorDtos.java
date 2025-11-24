package com.IMS.IssueManagementSystem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

public class ErrorDtos {

    @Data
    public static class ErrorResponse {
        private int code;
        private String message;
        private String status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date timestamp;
    }
}
