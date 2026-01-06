package com.pms.pmSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentCreateRequest {
    @NotNull(message = "Project Task ID is required")
    private Integer taskId;
    
    @NotNull(message = "Created by User ID is required")
    private Integer createdBy;
    
    @NotBlank(message = "Description is required")
    private String description;
}