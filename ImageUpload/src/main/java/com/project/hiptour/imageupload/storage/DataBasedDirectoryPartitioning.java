package com.project.hiptour.imageupload.storage;

import java.time.LocalDateTime;

public class DataBasedDirectoryPartitioning implements DirectoryPartitioning{
    @Override
    public String resolveDirectory(String storedName) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%02d/%02d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }
}
