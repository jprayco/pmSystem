package com.pms.pmSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.WorkBook;

@Repository
public interface WorkbookRepo extends JpaRepository<WorkBook, Integer>{

}
