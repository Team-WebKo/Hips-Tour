package com.project.hiptour.common.category.serviceImpl;

import com.project.hiptour.common.category.dto.CategoryResponseDto;
import com.project.hiptour.common.category.repository.CategoryRepository;
import com.project.hiptour.common.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
