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
public class IssueRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project ID is required")
    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    private Projects project;

    @NotNull(message = "Date raised is required")
    private Date date_raised;

    @NotBlank(message = "Raise By is required")
    private String raise_by;

    @NotBlank(message = "Issue is required")
    private String issue;

    private int priority;
    private int severity;
    private int status;
    private Date closure_date;
    private String remarks;

    @NotNull(message = "User ID is required")
    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Users created_by;

    @CreationTimestamp
    private Date timestamp;

}
