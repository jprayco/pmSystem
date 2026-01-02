package com.pms.pmSystem.dto;

import java.util.Date;

public class TaskResponseDTO {
    private Integer id;
    private Integer projectId;
    private String stage;
    private String name;
    private String description;
    private Integer completion_days;
    private Date start_date;
    private Date end_date;
    private Date completion_target_date;
    private Date completion_actual_date;
    private String dependencies;
    private Integer task_assignedUsersId;
    private String status;
    private String priority;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Integer id, Integer projectId, String stage, String name, String description,
                          Integer completion_days, Date start_date, Date end_date, Date completion_target_date,
                          Date completion_actual_date, String dependencies, Integer task_assignedUsersId,
                          String status, String priority) {
        this.id = id;
        this.projectId = projectId;
        this.stage = stage;
        this.name = name;
        this.description = description;
        this.completion_days = completion_days;
        this.start_date = start_date;
        this.end_date = end_date;
        this.completion_target_date = completion_target_date;
        this.completion_actual_date = completion_actual_date;
        this.dependencies = dependencies;
        this.task_assignedUsersId = task_assignedUsersId;
        this.status = status;
        this.priority = priority;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCompletion_days() { return completion_days; }
    public void setCompletion_days(Integer completion_days) { this.completion_days = completion_days; }

    public Date getStart_date() { return start_date; }
    public void setStart_date(Date start_date) { this.start_date = start_date; }

    public Date getEnd_date() { return end_date; }
    public void setEnd_date(Date end_date) { this.end_date = end_date; }

    public Date getCompletion_target_date() { return completion_target_date; }
    public void setCompletion_target_date(Date completion_target_date) { this.completion_target_date = completion_target_date; }

    public Date getCompletion_actual_date() { return completion_actual_date; }
    public void setCompletion_actual_date(Date completion_actual_date) { this.completion_actual_date = completion_actual_date; }

    public String getDependencies() { return dependencies; }
    public void setDependencies(String dependencies) { this.dependencies = dependencies; }

    public Integer getTask_assignedUsersId() { return task_assignedUsersId; }
    public void setTask_assignedUsersId(Integer task_assignedUsersId) { this.task_assignedUsersId = task_assignedUsersId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}