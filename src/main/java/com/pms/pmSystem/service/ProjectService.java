package com.pms.pmSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.Projects;
import com.pms.pmSystem.repository.ProjectRepo;

@Service
public class ProjectService {

    @Autowired
    ProjectRepo repo;

    public List<Projects> getAllProject() {
        return repo.findAll();
    }

    public Projects getProjectById(int id) {
        return repo.findById(id).orElse(new Projects());
    }

    public Projects createProject(Projects projects) {
        return repo.save(projects);
    }

    public Projects updateProject(int id, Projects updatedProject) {
        Projects existingProject = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        existingProject.setProj_name(updatedProject.getProj_name());
        existingProject.setProj_desc(updatedProject.getProj_desc());
        existingProject.setProj_code(updatedProject.getProj_code());
        existingProject.setProj_client_name(updatedProject.getProj_client_name());
        existingProject.setProj_po_date(updatedProject.getProj_po_date());
        existingProject.setProj_kick_off_date(updatedProject.getProj_kick_off_date());
        existingProject.setProj_implem_start_date(updatedProject.getProj_implem_start_date());
        existingProject.setProj_current_task(updatedProject.getProj_current_task());
        existingProject.setProj_current_task_status(updatedProject.getProj_current_task_status());
        existingProject.setProj_next_task(updatedProject.getProj_next_task());
        existingProject.setProj_next_task_target_start_date(updatedProject.getProj_next_task_target_start_date());
        existingProject.setProj_health(updatedProject.getProj_health());
        existingProject.setProj_resource(updatedProject.getProj_resource());
        existingProject.setProj_deply_date(updatedProject.getProj_deply_date());
        existingProject.setProj_training_date(updatedProject.getProj_training_date());
        existingProject.setProj_knowlegde_trans_date(updatedProject.getProj_knowlegde_trans_date());
        existingProject.setProj_cocso_date(updatedProject.getProj_cocso_date());
        //existingProject.setProj_overall_completion(updatedProject.getProj_overall_completion());

        return repo.save(existingProject);
    }


    public void deleteProject(int id){
         repo.deleteById(id);
    }

}
