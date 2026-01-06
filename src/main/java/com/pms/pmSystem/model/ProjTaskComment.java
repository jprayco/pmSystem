package com.pms.pmSystem.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class ProjTaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project Task ID is required")
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private ProjTask task_id;

    @NotNull(message = "Task Assigned User is required")
    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Users created_by;

    @NotBlank(message = "Description is required")
    private String description;

   @CreationTimestamp
    private Date timeStamp;

}
