package com.IMS.IssueManagementSystem.Exception;


import com.IMS.IssueManagementSystem.DTO.ErrorDtos;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorDtos.ErrorResponse errorResponse = new ErrorDtos.ErrorResponse();
        errorResponse.setCode(403);
        errorResponse.setStatus("Forbidden");
        errorResponse.setMessage("Forbidden: cannot access this resource");
        errorResponse.setTimestamp(new Date());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
