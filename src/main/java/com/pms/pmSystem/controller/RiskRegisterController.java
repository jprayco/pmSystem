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

import com.pms.pmSystem.dto.RiskRegisterDTO;
import com.pms.pmSystem.model.RiskRegister;
import com.pms.pmSystem.service.RiskRegisterService;

import jakarta.validation.Valid;

@RestController
public class RiskRegisterController {

    @Autowired
    RiskRegisterService service;

    @PostMapping("/project/risk-register")
    public ResponseEntity<?> create(@Valid @RequestBody RiskRegister rr) {
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

    @GetMapping("/project/{projId}/risk-register")
    public ResponseEntity<?> getByTaskId(@PathVariable int projId) {
        try {
            List<RiskRegister> rrs = service.getByProjectId(projId);

            List<RiskRegisterDTO> rrDTOs = rrs.stream().map(rr -> {
                Integer proj_id = rr.getProject() != null ? rr.getProject().getId() : null;
                return new RiskRegisterDTO(rr.getId(), proj_id, rr.getRef(), rr.getDescription(), rr.getCategory(), rr.getDate_identified(), rr.getOwner(), rr.getLikelihood(), rr.getImpact(), rr.getControl_plan(), rr.getResponse_status(), rr.getTimeStamp());
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Comments retrieved successfully");
            response.put("data", rrDTOs);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error retrieving comments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/project/risk-register/{id}")
    public ResponseEntity<?> updateComment(@PathVariable int id, @Valid @RequestBody RiskRegister rr) {
        try {

            RiskRegister updateRR = service.update(id, rr);

            Integer projId = updateRR.getProject() != null ? updateRR.getProject().getId() : null;

            RiskRegisterDTO responseDTO = new RiskRegisterDTO(
                    updateRR.getId(),
                    projId,
                    updateRR.getRef(),
                    updateRR.getDescription(),
                    updateRR.getCategory(),
                    updateRR.getDate_identified(),
                    updateRR.getOwner(),
                    updateRR.getLikelihood(),
                    updateRR.getImpact(),
                    updateRR.getControl_plan(),
                    updateRR.getResponse_status(),
                    updateRR.getTimeStamp()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", responseDTO);
            response.put("message", "Comment updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error updating comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/project/risk-register/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        try {
            service.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Comment deleted successfully!");
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
