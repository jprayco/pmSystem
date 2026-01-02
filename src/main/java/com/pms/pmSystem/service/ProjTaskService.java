package com.pms.pmSystem.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pms.pmSystem.model.ProjTask;
import com.pms.pmSystem.model.ProjTaskAttachment;
import com.pms.pmSystem.repository.ProjTaskAttachmentRepo;
import com.pms.pmSystem.repository.ProjTaskRepo;

@Service
public class ProjTaskService {

    @Autowired
    ProjTaskRepo repo;

    @Autowired
    ProjTaskAttachmentRepo attachmentRepo;

    private final String uploadDir = "src/main/resources/static/taskAttachment";

    public ProjTask create(ProjTask task, MultipartFile attachment) {
        ProjTask savedTask = repo.save(task);

        if (attachment != null && !attachment.isEmpty()) {
            saveAttachment(savedTask, attachment);
        }

        return savedTask;
    }

    private void saveAttachment(ProjTask task, MultipartFile file) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Save file to filesystem
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create and save attachment record
            ProjTaskAttachment attachment = new ProjTaskAttachment();
            attachment.setTask(task);
            attachment.setName(originalFileName);
            attachment.setPath(filePath.toString());

            attachmentRepo.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save attachment: " + e.getMessage());
        }
    }

    public List<ProjTask> getByProjectId(int projectId) {
        return repo.findByProjectId(projectId);
    }

    public ProjTask update(int id, ProjTask updated_task) {
        ProjTask existingTask = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        existingTask.setStage(updated_task.getStage());
        existingTask.setName(updated_task.getName());
        existingTask.setDescription(updated_task.getDescription());
        existingTask.setCompletion_days(updated_task.getCompletion_days());
        existingTask.setStart_date(updated_task.getStart_date());
        existingTask.setEnd_date(updated_task.getEnd_date());
        existingTask.setCompletion_target_date(updated_task.getCompletion_target_date());
        existingTask.setCompletion_actual_date(updated_task.getCompletion_actual_date());
        existingTask.setDependencies(updated_task.getDependencies());
        existingTask.setTask_assignedUsers(updated_task.getTask_assignedUsers());
        existingTask.setStatus(updated_task.getStatus());
        existingTask.setPriority(updated_task.getPriority());

        return repo.save(existingTask);
    }

    public ProjTask getById(int id) {
        Optional<ProjTask> taskOptional = repo.findById(id);
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    public void delete(int id) {
        List<ProjTaskAttachment> attachments = attachmentRepo.findByTaskId(id);

        // First delete all attachments
        for (ProjTaskAttachment attachment : attachments) {
            try {
                // Delete the physical file from disk
                Path filePath = Paths.get(attachment.getPath());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                // Log the error but continue with deletion
                System.out.println("Failed to delete attachment file: {} " + e.getMessage());
            }

            // Delete the attachment record from database
            attachmentRepo.delete(attachment);
        }

        // Then delete the workbook
        repo.deleteById(id);
    }
}
