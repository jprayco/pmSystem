package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.ProjTeamRoster;

@Repository
public interface ProjTeamRosterRepo extends JpaRepository<ProjTeamRoster, Integer>{
    @Query("SELECT ptr FROM ProjTeamRoster ptr WHERE ptr.project.id = :projectId")
    List<ProjTeamRoster> findByProjectId(@Param("projectId") int projectId);
}
