package com.pms.pmSystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.dto.ProjectAssignedDTO;
import com.pms.pmSystem.model.ProjectAssigned;
import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.repository.ProjectAssignedRepo;

@Service
public class ProjectAssignedService {

    @Autowired
    ProjectAssignedRepo repo;

    public ProjectAssigned create(ProjectAssigned pa) {
        return repo.save(pa);
    }

    public ProjectAssigned getByProjectId(int id) {
        return repo.findByProjectId(id);
    }

    public ProjectAssigned update(int id, ProjectAssigned updated_pa){
        ProjectAssigned existingPA = repo.findById(id)
            .orElseThrow(()-> new RuntimeException("Project not found with id : "+ id));

        existingPA.setProjectManager(updated_pa.getProjectManager());
        existingPA.setTechnicalTeamLead(updated_pa.getTechnicalTeamLead());
        existingPA.setAssistantTechLead(updated_pa.getAssistantTechLead());
        existingPA.setAccountManager(updated_pa.getAccountManager());

        return repo.save(existingPA);
    }

    public void delete(int id){
        repo.deleteById(id);
    }

    public ProjectAssignedDTO getProjectAssignedDTOByProjectId(int projectId) {
        ProjectAssigned projectAssigned = repo.findByProjectId(projectId);
        if (projectAssigned == null) {
            return null;
        }
        return convertToDTO(projectAssigned);
    }

    private ProjectAssignedDTO convertToDTO(ProjectAssigned projectAssigned) {
        // Create limited user maps with only required fields
        Map<String, Object> projectManager = createLimitedUserMap(projectAssigned.getProjectManager());
        Map<String, Object> technicalTeamLead = createLimitedUserMap(projectAssigned.getTechnicalTeamLead());
        Map<String, Object> assistantTechLead = createLimitedUserMap(projectAssigned.getAssistantTechLead());
        Map<String, Object> accountManager = createLimitedUserMap(projectAssigned.getAccountManager());

        return new ProjectAssignedDTO(
                projectAssigned.getId(),
                projectAssigned.getProject(),
                projectManager,
                technicalTeamLead,
                assistantTechLead,
                accountManager
        );
    }

    private Map<String, Object> createLimitedUserMap(Users user) {
        Map<String, Object> limitedUser = new HashMap<>();
        limitedUser.put("id", user.getId());
        limitedUser.put("fname", user.getFname());
        limitedUser.put("lname", user.getLname());
        limitedUser.put("email", user.getEmail());
        limitedUser.put("phone", user.getPhone());
        limitedUser.put("role", user.getRole());
        return limitedUser;
    }
}
