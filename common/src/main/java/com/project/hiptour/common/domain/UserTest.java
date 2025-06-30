package com.project.hiptour.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usertest")
@Getter
@NoArgsConstructor
public class UserTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "oauth_name", nullable = false)
    private short oauthName;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

}
