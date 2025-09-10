package com.project.hiptour.imageupload.controller;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
@TestPropertySource(properties = {"image.base-url=http://localhost:8080"})
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageService imageService;
    private final String baseUrl = "http://localhost:8080"; //테스트용 url


    @Test
    void 업로드_성공() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "hello".getBytes());
        ImageEntity image = new ImageEntity();
        image.setId(123L);
        image.setOriginalName("test.jpg");
        image.setPath("images/123.jpg");

        when(imageService.save(any())).thenReturn(image);

        mockMvc.perform(multipart("/images").file(mockFile))
                .andDo(result -> System.out.println("응답 JSON: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.originalName").value("test.jpg"))
                .andExpect(jsonPath("$.url").value(baseUrl + "/" + image.getPath()));
    }

    @Test
    void 업로드_실패_500() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "fail.jpg", "image/jpeg", "fail".getBytes());
        when(imageService.save(any())).thenThrow(new RuntimeException("파일 업로드에 실패했습니다."));

        mockMvc.perform(multipart("/images").file(mockFile))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("서버 오류가 발생했습니다."));
    }
}
