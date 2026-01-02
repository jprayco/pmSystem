package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.WorkbookAttachment;

@Repository
public interface WorkbookAttachmentRepo extends JpaRepository<WorkbookAttachment, Integer> {


 @Query("SELECT wa FROM WorkbookAttachment wa WHERE wa.workBook.id = :workbookId")
    List<WorkbookAttachment> findByWorkbookId(@Param("workbookId") int workbookId);
}
