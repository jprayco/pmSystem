package com.pms.pmSystem.model;

import java.util.Date;

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
public class WorkBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project ID is required")
    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    private Projects project;

    @NotNull(message = "Revision No. is required!")
    private int revisionNo;

    @NotNull(message = "Date is required!")
    private Date date;

    @NotBlank(message = "Changes is required")
    private String changes;

    @NotBlank(message = "Updater is required")
    private String updater;

    @NotNull(message = "Status is required")
    private int status;
}
