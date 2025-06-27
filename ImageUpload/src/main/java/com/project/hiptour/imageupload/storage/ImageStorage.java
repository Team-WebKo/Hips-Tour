package com.project.hiptour.imageupload.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.Closeable;


public interface ImageStorage extends Closeable {
    String save(MultipartFile file, String storedName) throws IOException;
    void delete(String fullPath) throws IOException;
    boolean exists(String fullPath);
}
