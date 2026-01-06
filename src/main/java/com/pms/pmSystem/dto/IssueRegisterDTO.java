package com.pms.pmSystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IssueRegisterDTO {
    private Integer id;
    private Integer projectId;
    private Date date_raised;
    private String raise_by;
    private String issue;
    private int priority;
    private int severity;
    private int status;
    private Date closure_date;
    private String remarks;
    private Integer created_by;
    private Date timestamp;
}
