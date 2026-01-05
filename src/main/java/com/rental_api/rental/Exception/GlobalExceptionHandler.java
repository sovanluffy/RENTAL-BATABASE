package com.rental_api.rental.Exception;

import com.rental_api.rental.Dtos.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> userNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> roleNotFound(RoleNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> conflict(ConflictException ex) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> badRequest(RuntimeException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "Bad Request", ex.getMessage()));
    }
}
