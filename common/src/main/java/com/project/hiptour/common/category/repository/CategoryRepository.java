package com.project.hiptour.common.category.repository;

import com.project.hiptour.common.entity.place.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
