package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import com.project.hiptour.imageupload.storage.ImageStorage;
import com.project.hiptour.imageupload.service.ImageValidator;
import com.project.hiptour.imageupload.storage.LocalImageStorage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageStorage imageStorage;

    public ImageService(ImageRepository imageRepository, ImageStorage imageStorage){
        this.imageRepository = imageRepository;
        this.imageStorage = imageStorage;
    }

    public ImageEntity save(MultipartFile file) {
        ImageValidator.validate(file); //유효성

        String uuid = UUID.randomUUID().toString();
        String storedName = uuid + ".jpg";
        String path = null;

        try {
            //전처리
            byte[] processed = ImageProcessor.processToJpg(file);

            //전처리 후의 파일저장
            path = imageStorage.save(processed , storedName);

            //db저장
            ImageEntity entity = new ImageEntity();
            entity.setOriginalName(file.getOriginalFilename());
            entity.setStoredName(storedName);
            entity.setPath(path);
            entity.setCreatedAt(LocalDateTime.now());

            return imageRepository.save(entity);

        } catch (IOException e) {
            //파일 생성 후 db저장에 실패한 경우
            if (path != null) {
                try {
                    imageStorage.delete(path);
                } catch (IOException ignored) {
                }
            }
        }
        throw new RuntimeException("파일 업로드에 실패했습니다.");
    }

    //이미지 다운로드할때 사용
    public ImageEntity getById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 이미지를 찾을 수 없습니다."));
    }

    //삭제 요청 들어오면 논리삭제
    public void delete(Long id) {
        ImageEntity image = getById(id);
        image.setDeletedAt(LocalDateTime.now());
        imageRepository.save(image); // 실제 삭제는 스케줄링
    }

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    @Transactional
    public void deleteMarkedImages() {
        List<ImageEntity> toDelete = imageRepository.findAllByDeletedAtIsNotNull();
        for (ImageEntity image : toDelete) {
            try {
                imageStorage.delete(image.getPath());
                imageRepository.delete(image);
                //logger변환
                System.out.println("완전 삭제 완료: " + image.getStoredName());
            } catch (IOException e) {
                System.out.println("파일 삭제 실패: " + image.getStoredName());
            }
        }
    }

}


