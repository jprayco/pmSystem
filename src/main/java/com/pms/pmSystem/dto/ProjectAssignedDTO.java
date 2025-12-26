package com.pms.pmSystem.dto;

import java.util.Map;

import com.pms.pmSystem.model.Projects;

public class ProjectAssignedDTO {
    private Integer id;
    private Projects project;
    private Map<String, Object> projectManager;
    private Map<String, Object> technicalTeamLead;
    private Map<String, Object> assistantTechLead;
    private Map<String, Object> accountManager;

    // Constructor
    public ProjectAssignedDTO(Integer id, Projects project, Map<String, Object> projectManager, 
                            Map<String, Object> technicalTeamLead, Map<String, Object> assistantTechLead, 
                            Map<String, Object> accountManager) {
        this.id = id;
        this.project = project;
        this.projectManager = projectManager;
        this.technicalTeamLead = technicalTeamLead;
        this.assistantTechLead = assistantTechLead;
        this.accountManager = accountManager;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Projects getProject() { return project; }
    public void setProject(Projects project) { this.project = project; }
    
    public Map<String, Object> getProjectManager() { return projectManager; }
    public void setProjectManager(Map<String, Object> projectManager) { this.projectManager = projectManager; }
    
    public Map<String, Object> getTechnicalTeamLead() { return technicalTeamLead; }
    public void setTechnicalTeamLead(Map<String, Object> technicalTeamLead) { this.technicalTeamLead = technicalTeamLead; }
    
    public Map<String, Object> getAssistantTechLead() { return assistantTechLead; }
    public void setAssistantTechLead(Map<String, Object> assistantTechLead) { this.assistantTechLead = assistantTechLead; }
    
    public Map<String, Object> getAccountManager() { return accountManager; }
    public void setAccountManager(Map<String, Object> accountManager) { this.accountManager = accountManager; }
}