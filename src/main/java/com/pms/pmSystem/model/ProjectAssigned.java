package com.pms.pmSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class ProjectAssigned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Project ID is required")
    @OneToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    private Projects project;

    @NotNull(message = "Project Manager is required")
    @OneToOne
    @JoinColumn(name = "pa_pm", referencedColumnName = "id")
    private Users projectManager;

    @NotNull(message = "Technical Team Lead is required")
    @OneToOne
    @JoinColumn(name = "pa_ttl", referencedColumnName = "id")
    private Users technicalTeamLead;

    @NotNull(message = "Asst Tech Lead is required")
    @OneToOne
    @JoinColumn(name = "pa_atl", referencedColumnName = "id")
    private Users assistantTechLead;

    @NotNull(message = "Account Manager is required")
    @OneToOne
    @JoinColumn(name = "pa_am", referencedColumnName = "id")
    private Users accountManager;

}
