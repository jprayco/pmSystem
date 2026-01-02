package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.ProjTask;

@Repository
public interface ProjTaskRepo extends JpaRepository<ProjTask, Integer>{
    @Query("SELECT task FROM ProjTask task WHERE task.project.id = :projectId")
    List<ProjTask> findByProjectId(@Param("projectId") int projectId);
}
