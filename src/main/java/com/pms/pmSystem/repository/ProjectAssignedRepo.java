package com.pms.pmSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.ProjectAssigned;

@Repository
public interface ProjectAssignedRepo extends JpaRepository<ProjectAssigned, Integer> {
    @Query("SELECT pa FROM ProjectAssigned pa WHERE pa.project.id = :projectId")
    ProjectAssigned findByProjectId(@Param("projectId") int projectId);

}
