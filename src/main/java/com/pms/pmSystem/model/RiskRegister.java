package com.pms.pmSystem.model;

import java.sql.Date;

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
public class RiskRegister {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project ID is required")
    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    private Projects project;

    @NotNull(message = "Project Task ID is required")
    private int ref;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Date Identified is required")
    private Date date_identified;

    @NotBlank(message = "Owner is required")
    private String owner;


    private Integer likelihood;
    private Integer impact;
    private String control_plan;
    private String response_status;
     @CreationTimestamp
    private Date timeStamp;
}
