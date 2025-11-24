package com.IMS.IssueManagementSystem.Exception;

import com.IMS.IssueManagementSystem.DTO.ErrorDtos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");


        ErrorDtos.ErrorResponse errorResponse = new ErrorDtos.ErrorResponse();
        errorResponse.setCode(401);
        errorResponse.setStatus("Unauthorized");
        errorResponse.setMessage("Unauthorized: cannot access this resource");
        errorResponse.setTimestamp(new Date());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
