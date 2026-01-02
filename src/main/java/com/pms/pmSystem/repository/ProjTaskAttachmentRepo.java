package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.ProjTaskAttachment;

@Repository
public interface ProjTaskAttachmentRepo extends JpaRepository<ProjTaskAttachment, Integer>{
    @Query("SELECT pta FROM ProjTaskAttachment pta WHERE pta.task.id = :taskId")
    List<ProjTaskAttachment> findByTaskId(@Param("taskId") int taskId);
}
