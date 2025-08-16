package com.project.hiptour.sync.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place")
public class TourPlace {
    @Id
    private String id;

    private String title;
    private String address;
    private String contentTypeId;
    private String imageUrl;
    private String description;

}
