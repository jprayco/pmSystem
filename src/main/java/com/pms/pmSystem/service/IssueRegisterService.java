package com.pms.pmSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.IssueRegister;
import com.pms.pmSystem.model.RiskRegister;
import com.pms.pmSystem.repository.IssueRegisterRepo;

import jakarta.validation.Valid;

@Service
public class IssueRegisterService {

    @Autowired
    IssueRegisterRepo repo;

    public IssueRegister create(IssueRegister ir) {
        return repo.save(ir);
    }

    public List<IssueRegister> getByProjectId(int id) {
        return repo.findByProjectId(id);
    }

    public IssueRegister update(int id, IssueRegister updated_rr) {
        IssueRegister existingIR = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id : " + id));

        existingIR.setProject(updated_rr.getProject());
        existingIR.setDate_raised(updated_rr.getDate_raised());
        existingIR.setRaise_by(updated_rr.getRaise_by());
        existingIR.setIssue(updated_rr.getIssue());
        existingIR.setPriority(updated_rr.getPriority());
        existingIR.setSeverity(updated_rr.getPriority());
        existingIR.setClosure_date(updated_rr.getClosure_date());
        existingIR.setRemarks(updated_rr.getRemarks());
        existingIR.setRemarks(updated_rr.getRemarks());

        return repo.save(existingIR);
    }

    public void delete(int id) {
      repo.deleteById(id);
    }
}
