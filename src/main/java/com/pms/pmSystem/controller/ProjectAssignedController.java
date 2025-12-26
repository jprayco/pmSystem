package com.pms.pmSystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.pmSystem.dto.ProjectAssignedDTO;
import com.pms.pmSystem.model.ProjectAssigned;
import com.pms.pmSystem.model.Projects;
import com.pms.pmSystem.service.ProjectAssignedService;

import jakarta.validation.Valid;

@RestController
public class ProjectAssignedController {

    @Autowired
    ProjectAssignedService service;

    @PostMapping("/project-assigned")
    public ResponseEntity<?> create(@Valid @RequestBody ProjectAssigned pa) {
        try {
            service.create(pa);
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Created!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error creating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/project-assigned/project/{projectId}")
    public ResponseEntity<?> getByProjectId(@PathVariable int projectId) {
        try {
            ProjectAssignedDTO projectAssigned = service.getProjectAssignedDTOByProjectId(projectId);
            if (projectAssigned == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
                errorResponse.put("message", "Project assignment not found for project ID: " + projectId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", projectAssigned);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error retrieving project assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/project-assigned/project/edit/{pa_Id}")
    public ResponseEntity<?> updateProject(@PathVariable int pa_Id, @Valid @RequestBody ProjectAssigned pa) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", service.update(pa_Id, pa));
            response.put("message", "Updated!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error updating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/project-assigned/project/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        try {
            service.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Project Assigned deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error deleting project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        errors.put("timestamp", System.currentTimeMillis());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Failed");
        errors.put("message", "One or more fields failed validation");
        errors.put("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }
}
