package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import com.project.hiptour.imageupload.storage.ImageStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
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
            log.error("이미지 저장 중 오류 발생: {}", e.getMessage(), e);

            //파일 생성 후 db저장에 실패
            if (path != null) {
                try {
                    imageStorage.delete(path);
                    log.warn("저장 실패한 파일 삭제 : {}", path);
                } catch (IOException ignored) {
                    log.error("파일 삭제 실패 - {}", e.getMessage(), e);
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
                log.info("파일 삭제 완료: {}", image.getPath());
            } catch (IOException e) {
                log.error("파일 삭제 실패: {}", e.getMessage(), e);
            }
        }
    }

}


