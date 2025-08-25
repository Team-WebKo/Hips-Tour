package com.project.hiptour.imageupload.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 이미지 전처리: 리사이징 및 JPG 포맷으로 변환
 */
public class ImageProcessor {

    public static byte[] processToJpg(MultipartFile file) throws IOException {
        // MultipartFile → BufferedImage로 변환
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // 썸네일 생성 (너비 1024px, 비율 유지)
        BufferedImage resized = Thumbnails.of(originalImage)
                .size(1024, Integer.MAX_VALUE) // 세로는 비율에 맞게
                .outputFormat("jpg")
                .asBufferedImage();

        //결과 이미지를 Byte 배열로 반환 (JPG)
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        ImageIO.write(resized, "jpg", by);
        return by.toByteArray();
    }
}
