package com.project.hiptour.common.category;

import com.project.hiptour.common.category.dto.CategoryResponseDto;
import com.project.hiptour.common.category.repository.CategoryRepository;
import com.project.hiptour.common.category.serviceImpl.CategoryServiceImpl;
import com.project.hiptour.common.entity.place.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("전체 카테고리 조회 - 성공")
    void getAllCategories_Success() {
        Category category1 = new Category(1, "카테고리1", "카테고리1 설명");
        Category category2 = new Category(2, "카테고리2", "카테고리2 설명");

        List<Category> categories = List.of(category1, category2);
        given(categoryRepository.findAll()).willReturn(categories);

        List< CategoryResponseDto> result = categoryServiceImpl.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryId()).isEqualTo(1);
        assertThat(result.get(0).getCategoryName()).isEqualTo("카테고리1");
        assertThat(result.get(1).getCategoryId()).isEqualTo(2);
        assertThat(result.get(1).getCategoryName()).isEqualTo("카테고리2");
    }
}
