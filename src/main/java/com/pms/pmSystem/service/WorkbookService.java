package com.pms.pmSystem.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pms.pmSystem.model.ProjectAssigned;
import com.pms.pmSystem.model.Projects;
import com.pms.pmSystem.model.WorkBook;
import com.pms.pmSystem.model.WorkbookAttachment;
import com.pms.pmSystem.repository.WorkbookAttachmentRepo;
import com.pms.pmSystem.repository.WorkbookRepo;

import jakarta.validation.Valid;

@Service
public class WorkbookService {

    @Autowired
    WorkbookRepo repo;

    @Autowired
    WorkbookAttachmentRepo workbookAttachmentRepo;

    @Autowired
    WorkbookAttachmentRepo attachmentRepo;

    private final String uploadDir = "src/main/resources/static/workbookAttachments";

    public WorkBook create(WorkBook wb, MultipartFile attachment) {
        // NOTE: You need to implement project retrieval here
        // For now, we'll assume the project is already set on the WorkBook object

        // Save workbook first
        WorkBook savedWorkbook = repo.save(wb);

        // Handle attachment if provided
        if (attachment != null && !attachment.isEmpty()) {
            saveAttachment(savedWorkbook, attachment);
        }

        return savedWorkbook;
    }

    public List<WorkBook> getByProjectId(int projectId) {
        return repo.findByProjectId(projectId);
    }

    private void saveAttachment(WorkBook workBook, MultipartFile file) {
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
            WorkbookAttachment attachment = new WorkbookAttachment();
            attachment.setWorkBook(workBook);
            attachment.setName(originalFileName);
            attachment.setPath(filePath.toString());

            attachmentRepo.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save attachment: " + e.getMessage());
        }
    }

    public WorkBook update(int id, WorkBook updated_wb) {
        WorkBook existingWB = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id : " + id));

        existingWB.setChanges(updated_wb.getChanges());
        existingWB.setDate(updated_wb.getDate());
        existingWB.setRevisionNo(updated_wb.getRevisionNo());
        existingWB.setUpdater(updated_wb.getUpdater());
        existingWB.setStatus(updated_wb.getStatus());

        return repo.save(existingWB);
    }

    public void delete(int id) {
        List<WorkbookAttachment> attachments = workbookAttachmentRepo.findByWorkbookId(id);

        // First delete all attachments
        for (WorkbookAttachment attachment : attachments) {
            try {
                // Delete the physical file from disk
                Path filePath = Paths.get(attachment.getPath());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                // Log the error but continue with deletion
                System.out.println("Failed to delete attachment file: {} "+e.getMessage());
            }

            // Delete the attachment record from database
            workbookAttachmentRepo.delete(attachment);
        }

        // Then delete the workbook
        repo.deleteById(id);
    }
}
