package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import com.project.hiptour.imageupload.storage.ImageStorage;
import com.project.hiptour.imageupload.service.ImageValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageStorage imageStorage;

    public ImageService(ImageRepository imageRepository, ImageStorage imageStorage){
        this.imageRepository = imageRepository;
        this.imageStorage = imageStorage;
    }

    @Transactional(rollbackFor = Exception.class)
    public ImageEntity save(MultipartFile file) {
        ImageValidator.validate(file);
        String uuid = UUID.randomUUID().toString();
        String storedName = uuid + "_" + file.getOriginalFilename();
        String path = null;

        try {
            //파일저장
            path = imageStorage.save(file, storedName);
            System.out.println("파일 저장 성공" + path);

            //db저장
            ImageEntity entity = new ImageEntity();
            entity.setOriginalName(file.getOriginalFilename());
            entity.setStoredName(storedName);
            entity.setPath(path);
            entity.setCreatedAt(LocalDateTime.now());

            return imageRepository.save(entity);

        } catch (IOException e) {
            System.out.println("파일 저장 실패:" + e.getMessage());

            //파일 생성 후 db저장에 실패한 경우
            if (path != null) {
                try {
                    imageStorage.delete(path);
                    System.out.println("저장 실패 파일 삭제:" + path);
                } catch (IOException deleteEx) {
                    System.out.println("파일 삭제 실패: " + deleteEx.getMessage());
                }
            }
        }
        throw new RuntimeException("파일 업로드에 실패했습니다.");
    }

    //이미지 다운로드할때 사용
    public ImageEntity getById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다."));
    }

    public void delete(Long id) {
        ImageEntity image = getById(id);

        try {
            imageStorage.delete(image.getPath());
            System.out.println("파일 삭제 완료: " + image.getPath());
        } catch (IOException e) {
            System.out.println("파일 삭제 실패: " + e.getMessage());
        }

        imageRepository.delete(image);
        System.out.println("이미지 DB 기록 삭제 완료: id=" + id);
    }
}


