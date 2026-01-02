package com.pms.pmSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.ProjTeamRoster;
import com.pms.pmSystem.repository.ProjTeamRosterRepo;

@Service
public class ProjTeamRosterService {

    @Autowired
    ProjTeamRosterRepo repo;

    public ProjTeamRoster create(ProjTeamRoster ptr) {
        return repo.save(ptr);
    }

    public List<ProjTeamRoster> getByProjectId(int id) {
        return repo.findByProjectId(id);
    }

    public ProjTeamRoster update(int id, ProjTeamRoster updated_ptr) {
        ProjTeamRoster existingPTR = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id : " + id));

        existingPTR.setCompany(updated_ptr.getCompany());
        existingPTR.setName(updated_ptr.getName());
        existingPTR.setRole(updated_ptr.getRole());
        existingPTR.setDepartment(updated_ptr.getDepartment());
        existingPTR.setPhone(updated_ptr.getPhone());
        existingPTR.setEmail(updated_ptr.getEmail());

        return repo.save(existingPTR);
    }

    public void deleteProject(int id){
        repo.deleteById(id);
    }
}
