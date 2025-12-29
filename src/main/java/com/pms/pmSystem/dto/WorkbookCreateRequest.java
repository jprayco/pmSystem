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
public class WorkbookCreateRequest {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "Revision No. is required!")
    private Integer revisionNo; // Fixed to match entity field name

    @NotNull(message = "Date is required!")
    private Date date;

    @NotBlank(message = "Changes is required")
    private String changes;

    @NotBlank(message = "Updater is required")
    private String updater;

    @NotNull(message = "Status is required")
    private Integer status;
}
