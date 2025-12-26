package com.pms.pmSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.Projects;

@Repository
public interface  ProjectRepo extends JpaRepository<Projects, Integer>{
    
}
