package com.project.hiptour.imageupload.storage;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataBasedDirectoryPartitioning implements DirectoryPartitioning{
    @Override
    public String resolveDirectory(String storedName) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%02d/%02d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }
}
