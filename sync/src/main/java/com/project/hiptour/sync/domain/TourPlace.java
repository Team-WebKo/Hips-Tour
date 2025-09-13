package com.project.hiptour.sync.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place")
public class TourPlace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String contentId;

    private String title;
    private String address;
    private String contentTypeId;
    private String areaCode;
    private String imageUrl;
    private String description;
}