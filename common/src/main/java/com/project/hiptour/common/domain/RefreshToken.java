package com.project.hiptour.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

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
    private Long kakaoid;

    @Column(name = "issuedat", nullable = false)
    private LocalDateTime issuedat;

    @Column(name = "expireat", nullable = false)
    private LocalDateTime expireat;

    public RefreshToken(Long kakaoid, LocalDateTime issuedat, LocalDateTime expireat){
        this.kakaoid = kakaoid;
        this.issuedat = issuedat;
        this.expireat = expireat;
    }

}
