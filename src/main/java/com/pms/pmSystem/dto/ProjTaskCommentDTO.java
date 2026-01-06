package com.pms.pmSystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProjTaskCommentDTO {
    private Integer id;
    private Integer taskId;
    private Integer createdById;
    private String createdByFullName;
    private String description;
    private Date timeStamp;
}