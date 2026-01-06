package com.pms.pmSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.RiskRegister;
import com.pms.pmSystem.repository.RiskRegisterRepo;

@Service
public class RiskRegisterService {

    @Autowired
    RiskRegisterRepo repo;

    public RiskRegister create(RiskRegister rr) {
        return repo.save(rr);
    }

    public List<RiskRegister> getByProjectId(int id) {
        return repo.findByProjectId(id);
    }

    public RiskRegister update(int id, RiskRegister updated_rr) {
        RiskRegister existingRR = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id : " + id));

        existingRR.setRef(updated_rr.getRef());
        existingRR.setDescription(updated_rr.getDescription());
        existingRR.setCategory(updated_rr.getCategory());
        existingRR.setDate_identified(updated_rr.getDate_identified());
        existingRR.setOwner(updated_rr.getOwner());
        existingRR.setLikelihood(updated_rr.getLikelihood());
        existingRR.setImpact(updated_rr.getImpact());
        existingRR.setControl_plan(updated_rr.getControl_plan());
        existingRR.setResponse_status(updated_rr.getResponse_status());

        return repo.save(existingRR);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

}
