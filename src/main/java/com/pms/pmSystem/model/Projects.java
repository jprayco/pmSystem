package com.pms.pmSystem.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Projects {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message="Project Name is required")
    private String proj_name;

    @NotBlank(message="Project Description is required")
    private String proj_desc;

    @NotBlank(message="Project Code is required")
    private String proj_code;

    @NotBlank(message="Project Client Name is required")
    private String proj_client_name;

    @NotNull(message="Project PO Date is required")
    private Date proj_po_date;

    private Date proj_kick_off_date;
    private Date proj_implem_start_date;
    private String proj_current_task;
    private String proj_current_task_status;
    private String proj_next_task;
    private String proj_next_task_target_start_date;
    private String proj_health;
    private String proj_resource;
    private Date proj_deply_date;
    private Date proj_training_date;
    private Date proj_knowlegde_trans_date;
    private Date proj_cocso_date;
    private String proj_overall_completion;

    @NotNull(message="Project Contract Period Date is required")
    private Date proj_contract_period_date;

    @Override
    public String toString() {
        return "Projects{" +
                "id=" + id +
                ", proj_name='" + proj_name + '\'' +
                ", proj_desc='" + proj_desc + '\'' +
                ", proj_code='" + proj_code + '\'' +
                ", proj_client_name='" + proj_client_name + '\'' +
                ", proj_po_date=" + proj_po_date +
                ", proj_kick_off_date=" + proj_kick_off_date +
                ", proj_implem_start_date=" + proj_implem_start_date +
                ", proj_current_task='" + proj_current_task + '\'' +
                ", proj_current_task_status='" + proj_current_task_status + '\'' +
                ", proj_next_task='" + proj_next_task + '\'' +
                ", proj_next_task_target_start_date='" + proj_next_task_target_start_date + '\'' +
                ", proj_health='" + proj_health + '\'' +
                ", proj_resource='" + proj_resource + '\'' +
                ", proj_deply_date=" + proj_deply_date +
                ", proj_training_date=" + proj_training_date +
                ", proj_knowlegde_trans_date=" + proj_knowlegde_trans_date +
                ", proj_cocso_date=" + proj_cocso_date +
                ", proj_overall_completion=" + proj_overall_completion +
                ", proj_contract_period_date=" + proj_contract_period_date +
                '}';
    }

}
