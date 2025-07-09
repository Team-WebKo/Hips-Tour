package com.project.hiptour.imageupload.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter

public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;   // 사용자가 업로드한 파일의 원래 이름
    private String storedName;     //  서버에 저장된 파일명 (UUID 설정)
    private String path;           //  파일 시스템 상 위치
    private LocalDateTime createdAt; // 업로드된 시간
    private LocalDateTime deletedAt;

}
