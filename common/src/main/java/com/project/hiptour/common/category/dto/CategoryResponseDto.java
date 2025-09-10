package com.project.hiptour.common.category.dto;

import com.project.hiptour.common.entity.place.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private int categoryId;
    private String categoryName;

    public static CategoryResponseDto fromEntity(Category category) {
        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName());
    }
}
