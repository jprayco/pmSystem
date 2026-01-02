package com.pms.pmSystem.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pms.pmSystem.dto.WorkbookCreateRequest;
import com.pms.pmSystem.model.Projects;
import com.pms.pmSystem.model.WorkBook;
import com.pms.pmSystem.service.ProjectService;
import com.pms.pmSystem.service.WorkbookService;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
public class WorkbookController {

    @Autowired
    WorkbookService service;

    @Autowired
    ProjectService projectService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/project/workbook", consumes = {"multipart/form-data"})
    public ResponseEntity<?> create(
            @RequestPart("workbook") String workbookJson,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) {

        try {
            // Parse JSON string to WorkbookCreateRequest
            WorkbookCreateRequest request = objectMapper.readValue(workbookJson, WorkbookCreateRequest.class);

            // Get the project by ID
            Projects project = projectService.getProjectById(request.getProjectId());
            if (project == null || project.getId() == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Project not found with id: " + request.getProjectId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Convert to WorkBook entity
            WorkBook workBook = new WorkBook();
            workBook.setProject(project);
            workBook.setRevisionNo(request.getRevisionNo());
            workBook.setDate(request.getDate());
            workBook.setChanges(request.getChanges());
            workBook.setUpdater(request.getUpdater());
            workBook.setStatus(request.getStatus());

            WorkBook savedWorkbook = service.create(workBook, attachment);
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Created!");
            response.put("workbook", savedWorkbook);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error creating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/project/{projectId}/workbook")
    public ResponseEntity<?> getByProjectId(@PathVariable int projectId) {
        try {
            List<WorkBook> workbooks = service.getByProjectId(projectId);

            if (workbooks.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("statusCode", HttpStatus.NOT_FOUND.value());
                response.put("message", "No workbooks found for project id: " + projectId);
                response.put("workbooks", workbooks);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Workbooks retrieved successfully");
            response.put("workbooks", workbooks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error retrieving workbooks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/project/{id}/workbook/edit")
    public ResponseEntity<?> updateProject(@PathVariable int id, @Valid @RequestBody WorkBook wb) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", service.update(id, wb));
            response.put("message", "Updated!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error updating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/project/workbook/{id}")
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
