package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.ProjTaskComment;

@Repository
public interface ProjTaskCommentRepo extends JpaRepository<ProjTaskComment, Integer> {

    @Query("SELECT comm FROM ProjTaskComment comm WHERE comm.task_id.id = :taskId")
    List<ProjTaskComment> findByTaskId(@Param("taskId") Integer taskId);
}
