package com.pms.pmSystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.service.authentication.UserService;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users users) {
        Map<String, Object> response = new HashMap<>();

        users.setStatus(0);

        response.put("statusCode", HttpStatus.OK.value());
        response.put("data", service.register(users));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Users users) {
        try {
            String token = service.verify(users);

            Map<String, Object> response = new HashMap<>();
            Map<String, String> data = new HashMap<>();

            data.put("token", token);
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", "Authentication failed");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error
                -> fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        errors.put("timestamp", System.currentTimeMillis());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Failed");
        errors.put("message", "One or more fields failed validation");
        errors.put("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }

}
