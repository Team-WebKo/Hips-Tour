package com.project.hiptour.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refreshtoken")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kakaoid", nullable = false)
    private Long kakaoId;

    @Column(name = "token", nullable = false, length = 500)
    private String token;

    @Column(name = "issuedat", nullable = false)
    private LocalDateTime issuedat;

    @Column(name = "expireat", nullable = false)
    private LocalDateTime expireat;

    public RefreshToken(Long kakaoId, String token, LocalDateTime issuedat, LocalDateTime expireat){
        this.kakaoId = kakaoId;
        this.token = token;
        this.issuedat = issuedat;
        this.expireat = expireat;
    }

    public String getToken() {return this.token;}
}
