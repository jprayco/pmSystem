package com.pms.pmSystem.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class ProjTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project ID is required")
    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    private Projects project;

    @NotBlank(message = "Stage is required")
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

    @NotNull(message = "Task Assigned User is required")
    @ManyToOne
    @JoinColumn(name = "task_assignedUsers", referencedColumnName = "id")
    private Users task_assignedUsers;

    @NotBlank(message = "Status is required")
    private String status;

    private String priority;

    @NotNull(message = "Task Assigned User is required")
    @OneToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Users created_by;
}
