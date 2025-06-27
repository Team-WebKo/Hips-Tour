package com.project.hiptour.imageupload.controller;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    void 업로드_성공() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "hello".getBytes());
        ImageEntity image = new ImageEntity();
        image.setId(123L);
        image.setOriginalName("test.jpg");

        when(imageService.save(any())).thenReturn(image);

        mockMvc.perform(multipart("/images").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("/images/123"));
    }

    @Test
    void 업로드_실패_500() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "fail.jpg", "image/jpeg", "fail".getBytes());
        when(imageService.save(any())).thenThrow(new RuntimeException("파일 업로드에 실패했습니다."));

        mockMvc.perform(multipart("/images").file(mockFile))
                .andExpect(status().isInternalServerError());
    }
}
