package com.pms.pmSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.WorkbookAttachment;

@Repository
public interface WorkbookAttachmentRepo extends JpaRepository<WorkbookAttachment, Integer>{

}
