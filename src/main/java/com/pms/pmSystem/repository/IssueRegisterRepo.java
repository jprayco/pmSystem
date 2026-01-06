package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.IssueRegister;

@Repository
public interface IssueRegisterRepo extends JpaRepository<IssueRegister, Integer> {

    @Query("SELECT issue FROM IssueRegister issue WHERE issue.project.id = :projectId")
    List<IssueRegister> findByProjectId(@Param("projectId") int projectId);
}
