package com.project.hiptour.imageupload.service;


import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageProcessor {
    private static final Logger log = LoggerFactory.getLogger(ImageProcessor.class);

    public static byte[] processToJpg(MultipartFile file) throws IOException {
        log.info("ImageProcessor - start: name={}, reportedSize={}", file.getOriginalFilename(), file.getSize());
        // 전체 바이트를 먼저 읽음
        byte[] raw = file.getBytes();
        log.info("ImageProcessor - raw bytes length={}", raw.length);

        // BufferedImage 로 읽어서 null 체크 및 크기 로그
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(raw));
        if (original == null) {
            log.error("ImageProcessor - ImageIO.read returned null for file {}", file.getOriginalFilename());
            throw new IOException("이미지를 읽을 수 없습니다. (지원되지 않는 포맷이거나 손상된 파일)");
        }
        log.info("ImageProcessor - original width={}, height={}", original.getWidth(), original.getHeight());

        // Thumbnailator로 JPG 변환
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Thumbnails.of(new ByteArrayInputStream(raw))
                    .size(1024, Integer.MAX_VALUE)
                    .outputFormat("jpg")
                    .toOutputStream(baos);

            byte[] out = baos.toByteArray();
            log.info("ImageProcessor - processed bytes length={}", out.length);

            if (out.length == 0) {
                throw new IOException("처리된 이미지가 0바이트입니다.");
            }
            return out;
        } catch (IOException e) {
            log.error("ImageProcessor - 처리 실패: {}", e.getMessage(), e);
            throw e;
        }
    }
}
