package com.project.hiptour.common.category.service;

import com.project.hiptour.common.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
}
