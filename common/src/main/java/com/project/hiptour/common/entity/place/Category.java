package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Category")
public class Category extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Integer categoryId;

    @Column(name = "categoryname", nullable = false)
    private String categoryName;
    private String description;

    @Builder
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
}
