package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Getter
public class Category extends BaseUpdateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String categoryName;
    private String description;


}
