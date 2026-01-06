package com.pms.pmSystem.repository;

import com.pms.pmSystem.model.ProjTask;
import com.pms.pmSystem.model.RiskRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskRegisterRepo extends JpaRepository<RiskRegister, Integer> {
    @Query("SELECT risk FROM RiskRegister risk WHERE risk.project.id = :projectId")
    List<RiskRegister> findByProjectId(@Param("projectId") int projectId);
}
