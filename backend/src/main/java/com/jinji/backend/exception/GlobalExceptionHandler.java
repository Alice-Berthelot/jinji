package com.jinji.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "RESOURCE_NOT_FOUND");
        body.put("message", ex.getMessage());
        body.put("status", 404);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ResourceAlreadyExistsException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "RESOURCE_ALREADY_EXISTS");
        body.put("field", ex.getField());
        body.put("message", ex.getMessage());
        body.put("status", 409);
        body.put("timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "BAD_REQUEST");
        body.put("message", ex.getMessage());
        body.put("status", 400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "INTERNAL_ERROR");
        body.put("message", "Une erreur est survenue");
        body.put("status", 500);
        body.put("timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(ForbiddenException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "FORBIDDEN");
        body.put("message", ex.getMessage());
        body.put("status", 403);
        body.put("timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "FORBIDDEN");
        body.put("message", "Access denied");
        body.put("status", 403);
        body.put("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "UNAUTHORIZED");
        body.put("message", "Authentication required");
        body.put("status", 401);
        body.put("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}