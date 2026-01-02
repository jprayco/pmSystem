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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pms.pmSystem.dto.TaskCreateRequest;
import com.pms.pmSystem.dto.TaskResponseDTO;
import com.pms.pmSystem.model.ProjTask;
import com.pms.pmSystem.model.Projects;
import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.repository.UserRepo;
import com.pms.pmSystem.service.ProjTaskService;
import com.pms.pmSystem.service.ProjectService;
import com.pms.pmSystem.service.authentication.UserService;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
public class ProjTaskController {

    @Autowired
    ProjTaskService service;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/project/task", consumes = {"multipart/form-data"})
    public ResponseEntity<?> create(
            @RequestPart("task") String taskJson,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) {

        try {
            TaskCreateRequest request = objectMapper.readValue(taskJson, TaskCreateRequest.class);

            // Get the project by ID
            Projects project = projectService.getProjectById(request.getProjectId());
            if (project == null || project.getId() == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Project not found with id: " + request.getProjectId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Convert to ProjTask entity
            ProjTask task = new ProjTask();
            task.setProject(project);
            task.setStage(request.getStage());
            task.setName(request.getName());
            task.setDescription(request.getDescription());
            task.setCompletion_days(request.getCompletion_days());
            task.setStart_date(request.getStart_date());
            task.setEnd_date(request.getEnd_date());
            task.setCompletion_target_date(request.getCompletion_target_date());
            task.setCompletion_actual_date(request.getCompletion_actual_date());
            task.setDependencies(request.getDependencies());

            // Convert assignedUserId to Users object
            if (request.getAssignedUserId() != null) {
                Optional<Users> userOptional = userRepo.findById(request.getAssignedUserId());
                if (userOptional.isPresent()) {
                    task.setTask_assignedUsers(userOptional.get());
                } else {
                    // Handle case where user is not found
                    System.err.println("User not found with ID: " + request.getAssignedUserId());
                }
            }

            task.setStatus(request.getStatus());
            task.setPriority(request.getPriority());

            ProjTask savedTask = service.create(task, attachment);

            // Convert to TaskResponseDTO with only IDs instead of full objects
            Integer assignedUserId = savedTask.getTask_assignedUsers() != null ? savedTask.getTask_assignedUsers().getId() : null;
            Integer projectId = savedTask.getProject() != null ? savedTask.getProject().getId() : null;

            TaskResponseDTO responseDTO = new TaskResponseDTO(
                    savedTask.getId(),
                    projectId,
                    savedTask.getStage(),
                    savedTask.getName(),
                    savedTask.getDescription(),
                    savedTask.getCompletion_days(),
                    savedTask.getStart_date(),
                    savedTask.getEnd_date(),
                    savedTask.getCompletion_target_date(),
                    savedTask.getCompletion_actual_date(),
                    savedTask.getDependencies(),
                    assignedUserId,
                    savedTask.getStatus(),
                    savedTask.getPriority()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Created!");
            response.put("data", responseDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error creating task: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/project/{projectId}/task")
    public ResponseEntity<?> getByProjectId(@PathVariable int projectId) {
        try {
            List<ProjTask> tasks = service.getByProjectId(projectId);

            // Convert ProjTask entities to TaskResponseDTO
            List<TaskResponseDTO> taskDTOs = tasks.stream().map(task -> {
                Integer assignedUserId = task.getTask_assignedUsers() != null ? task.getTask_assignedUsers().getId() : null;
                Integer projectIdFromTask = task.getProject() != null ? task.getProject().getId() : null;

                return new TaskResponseDTO(
                        task.getId(),
                        projectIdFromTask,
                        task.getStage(),
                        task.getName(),
                        task.getDescription(),
                        task.getCompletion_days(),
                        task.getStart_date(),
                        task.getEnd_date(),
                        task.getCompletion_target_date(),
                        task.getCompletion_actual_date(),
                        task.getDependencies(),
                        assignedUserId,
                        task.getStatus(),
                        task.getPriority()
                );
            }).collect(Collectors.toList());

            if (taskDTOs.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("statusCode", HttpStatus.NOT_FOUND.value());
                response.put("message", "No task found for project id: " + projectId);
                response.put("data", taskDTOs);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "task retrieved successfully");
            response.put("data", taskDTOs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error retrieving task: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/project/task/{id}/edit")
    public ResponseEntity<?> updateProject(@PathVariable int id, @Valid @RequestBody TaskCreateRequest request) {
        try {
            // Get the existing task
            ProjTask existingTask = service.getById(id);
            if (existingTask == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
                errorResponse.put("message", "Task not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            // Update the task with new values from the request
            existingTask.setStage(request.getStage());
            existingTask.setName(request.getName());
            existingTask.setDescription(request.getDescription());
            existingTask.setCompletion_days(request.getCompletion_days());
            existingTask.setStart_date(request.getStart_date());
            existingTask.setEnd_date(request.getEnd_date());
            existingTask.setCompletion_target_date(request.getCompletion_target_date());
            existingTask.setCompletion_actual_date(request.getCompletion_actual_date());
            existingTask.setDependencies(request.getDependencies());
            existingTask.setStatus(request.getStatus());
            existingTask.setPriority(request.getPriority());

            // Update project if projectId is provided
            if (request.getProjectId() != null) {
                Projects project = projectService.getProjectById(request.getProjectId());
                if (project == null || project.getId() == null) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    errorResponse.put("message", "Project not found with id: " + request.getProjectId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
                existingTask.setProject(project);
            }

            // Update assigned user if assignedUserId is provided
            if (request.getAssignedUserId() != null) {
                Optional<Users> userOptional = userRepo.findById(request.getAssignedUserId());
                if (userOptional.isPresent()) {
                    existingTask.setTask_assignedUsers(userOptional.get());
                } else {
                    // Handle case where user is not found
                    System.err.println("User not found with ID: " + request.getAssignedUserId());
                    existingTask.setTask_assignedUsers(null);
                }
            } else {
                existingTask.setTask_assignedUsers(null);
            }

            ProjTask updatedTask = service.update(id, existingTask);

            // Convert to TaskResponseDTO with only IDs instead of full objects
            Integer assignedUserId = updatedTask.getTask_assignedUsers() != null ? updatedTask.getTask_assignedUsers().getId() : null;
            Integer projectId = updatedTask.getProject() != null ? updatedTask.getProject().getId() : null;

            TaskResponseDTO responseDTO = new TaskResponseDTO(
                    updatedTask.getId(),
                    projectId,
                    updatedTask.getStage(),
                    updatedTask.getName(),
                    updatedTask.getDescription(),
                    updatedTask.getCompletion_days(),
                    updatedTask.getStart_date(),
                    updatedTask.getEnd_date(),
                    updatedTask.getCompletion_target_date(),
                    updatedTask.getCompletion_actual_date(),
                    updatedTask.getDependencies(),
                    assignedUserId,
                    updatedTask.getStatus(),
                    updatedTask.getPriority()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("data", responseDTO);
            response.put("message", "Updated!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Error updating task: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/project/task/{id}")
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
