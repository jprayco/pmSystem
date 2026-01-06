package com.pms.pmSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.ProjTaskComment;
import com.pms.pmSystem.repository.ProjTaskCommentRepo;

@Service
public class ProjTaskCommentService {

    @Autowired
    ProjTaskCommentRepo repo;

    public ProjTaskComment create(ProjTaskComment comment) {
        return repo.save(comment);
    }

    public List<ProjTaskComment> getByTaskId(int id) {
        return repo.findByTaskId(id);
    }

    public ProjTaskComment update(int id, ProjTaskComment updatedTaskComment) {
        ProjTaskComment existingTaskComment = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id : " + id));

        existingTaskComment.setTask_id(updatedTaskComment.getTask_id());
        existingTaskComment.setCreated_by(updatedTaskComment.getCreated_by());
        existingTaskComment.setDescription(updatedTaskComment.getDescription());
        return repo.save(existingTaskComment);
    }

    public ProjTaskComment getById(int id) {
        Optional<ProjTaskComment> commentOptional = repo.findById(id);
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

}
