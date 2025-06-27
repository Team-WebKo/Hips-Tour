package com.project.hiptour.imageupload.respository;

import com.project.hiptour.imageupload.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    // 아직 삭제되지 않은 이미지만 조회
    Optional<ImageEntity> findByIdAndDeletedAtIsNull(Long id);

    // 삭제 예약된 이미지 조회 (스케줄링용)
    List<ImageEntity> findAllByDeletedAtIsNotNull();
}
