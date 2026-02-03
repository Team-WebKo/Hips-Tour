package com.project.hiptour.common.entity.place.repos;

import com.project.hiptour.common.entity.place.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
