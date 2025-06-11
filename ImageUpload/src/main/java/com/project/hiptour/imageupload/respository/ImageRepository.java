package com.project.hiptour.imageupload.respository;

import com.project.hiptour.imageupload.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {}
