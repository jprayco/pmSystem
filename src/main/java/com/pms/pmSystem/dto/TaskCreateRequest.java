package com.pms.pmSystem.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "Stage is required!")
    private String stage;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private Integer completion_days;
    private Date start_date;
    private Date end_date;
    private Date completion_target_date;
    private Date completion_actual_date;
    private String dependencies;

    private Integer assignedUserId;

    @NotBlank(message = "Status is required")
    private String status;

    private String priority;
}
