package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class UserInfo extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long userId;
    @Column(unique = true)
    private String email;
    private String nickName;
    private String userIdentifier;

    @Builder
    public UserInfo(String email, String nickName, String userIdentifier) {
        this.email = email;
        this.nickName = nickName;
        this.userIdentifier = userIdentifier;
    }
}
