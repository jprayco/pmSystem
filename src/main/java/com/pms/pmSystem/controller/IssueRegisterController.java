package com.pms.pmSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.pms.pmSystem.dto.IssueRegisterDTO;
import com.pms.pmSystem.model.IssueRegister;
import com.pms.pmSystem.service.IssueRegisterService;

import jakarta.validation.Valid;

@RestController
public class IssueRegisterController {

    @Autowired
    IssueRegisterService service;

    @PostMapping("/project/issue-register")
    public ResponseEntity<?> create(@Valid @RequestBody IssueRegister rr) {
        try {
            service.create(rr);
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

    @GetMapping("/project/{projId}/issue-register")
    public ResponseEntity<?> getByTaskId(@PathVariable int projId) {
        try {
            List<IssueRegister> irs = service.getByProjectId(projId);

            List<IssueRegisterDTO> irDTOs = irs.stream().map(ir -> {
                Integer proj_id = ir.getProject() != null ? ir.getProject().getId() : null;
                Integer createdBy_id = ir.getCreated_by() != null ? ir.getCreated_by().getId() : null;
                return new IssueRegisterDTO(
                        ir.getId(),
                        proj_id,
                        ir.getDate_raised(),
                        ir.getRaise_by(),
                        ir.getIssue(),
                        ir.getPriority(),
                        ir.getSeverity(),
                        ir.getStatus(),
                        ir.getClosure_date(),
                        ir.getRemarks(),
                        createdBy_id,
                        ir.getTimestamp());
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Issues retrieved successfully");
            response.put("data", irDTOs);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error retrieving Issues: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/project/issue-register/{id}")
    public ResponseEntity<?> updateIssue(@PathVariable int id, @Valid @RequestBody IssueRegister rr) {
        try {

            IssueRegister updateRR = service.update(id, rr);

            Integer projId = updateRR.getProject() != null ? updateRR.getProject().getId() : null;
            Integer createdBy_id = updateRR.getCreated_by() != null ? updateRR.getCreated_by().getId() : null;

            IssueRegisterDTO responseDTO = new IssueRegisterDTO(
                    updateRR.getId(),
                    projId,
                    updateRR.getDate_raised(),
                    updateRR.getRaise_by(),
                    updateRR.getIssue(),
                    updateRR.getPriority(),
                    updateRR.getSeverity(),
                    updateRR.getStatus(),
                    updateRR.getClosure_date(),
                    updateRR.getRemarks(),
                    createdBy_id,
                    updateRR.getTimestamp()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", responseDTO);
            response.put("message", "Issue updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error updating Issue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/project/issue-register/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        try {
            service.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Issue deleted successfully!");
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
