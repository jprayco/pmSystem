package com.pms.pmSystem.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RiskRegisterDTO {
    private Integer id;
    private Integer projectId;
    private int ref;
    private String description;
    private String category;
    private Date date_identified;
    private String owner;
    private Integer likelihood;
    private Integer impact;
    private String control_plan;
    private String response_status;
    private Date timeStamp;
}
