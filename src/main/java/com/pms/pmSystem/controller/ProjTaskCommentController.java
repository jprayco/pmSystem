package com.pms.pmSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.pms.pmSystem.dto.ProjTaskCommentDTO;
import com.pms.pmSystem.dto.TaskCommentCreateRequest;
import com.pms.pmSystem.model.ProjTask;
import com.pms.pmSystem.model.ProjTaskComment;
import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.repository.ProjTaskRepo;
import com.pms.pmSystem.repository.UserRepo;
import com.pms.pmSystem.service.ProjTaskCommentService;
import com.pms.pmSystem.service.ProjTaskService;
import com.pms.pmSystem.service.authentication.UserService;

import jakarta.validation.Valid;

@RestController
public class ProjTaskCommentController {

    @Autowired
    ProjTaskCommentService service;

    @Autowired
    ProjTaskService projTaskService;

    @Autowired
    ProjTaskRepo projTaskRepo;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/project/task/comment")
    public ResponseEntity<?> create(@Valid @RequestBody ProjTaskComment comment) {
        try {
            service.create(comment);
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

    @GetMapping("/project/task/{taskId}/comment")
    public ResponseEntity<?> getByTaskId(@PathVariable int taskId) {
        try {
            List<ProjTaskComment> comments = service.getByTaskId(taskId);

            List<ProjTaskCommentDTO> commentDTOs = comments.stream().map(comment -> {
                Integer commentTaskId = comment.getTask_id() != null ? comment.getTask_id().getId() : null;

                Integer createdById = comment.getCreated_by() != null ? comment.getCreated_by().getId() : null;
                String createdByFullName = comment.getCreated_by() != null
                        ? (comment.getCreated_by().getFname() + " " + comment.getCreated_by().getLname()) : null;

                return new ProjTaskCommentDTO(comment.getId(), commentTaskId, createdById, createdByFullName, comment.getDescription(), comment.getTimeStamp());
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Comments retrieved successfully");
            response.put("data", commentDTOs);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error retrieving comments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/project/task/comment/{id}/edit")
    public ResponseEntity<?> updateComment(@PathVariable int id, @Valid @RequestBody TaskCommentCreateRequest request) {
        try {
            // Get the existing comment
            ProjTaskComment existingComment = service.getById(id);
            if (existingComment == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
                errorResponse.put("message", "Comment not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            // Update task_id if taskId is provided
            if (request.getTaskId() != null) {
                Optional<ProjTask> taskOptional = projTaskRepo.findById(request.getTaskId());
                if (taskOptional.isPresent()) {
                    existingComment.setTask_id(taskOptional.get());
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    errorResponse.put("message", "Comment not found with ID: " + request.getTaskId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }

            // Update created_by user if createdBy is provided
            if (request.getCreatedBy() != null) {
                Optional<Users> createdByUserOptional = userRepo.findById(request.getCreatedBy());
                if (createdByUserOptional.isPresent()) {
                    existingComment.setCreated_by(createdByUserOptional.get());
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    errorResponse.put("message", "Created by user not found with ID: " + request.getCreatedBy());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }

            // Update description
            if (request.getDescription() != null) {
                existingComment.setDescription(request.getDescription());
            }

            ProjTaskComment updatedComment = service.update(id, existingComment);

            // Convert to ProjTaskCommentDTO
            Integer commentTaskId = updatedComment.getTask_id() != null ? updatedComment.getTask_id().getId() : null;
            Integer createdById = updatedComment.getCreated_by() != null ? updatedComment.getCreated_by().getId() : null;
            String createdByFullName = updatedComment.getCreated_by() != null
                    ? (updatedComment.getCreated_by().getFname() + " " + updatedComment.getCreated_by().getLname()) : null;

            ProjTaskCommentDTO responseDTO = new ProjTaskCommentDTO(
                    updatedComment.getId(),
                    commentTaskId,
                    createdById,
                    createdByFullName,
                    updatedComment.getDescription(),
                    updatedComment.getTimeStamp()
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

    @DeleteMapping("/project/task/comment/{id}")
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
