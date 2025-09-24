package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.*;

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
    public UserInfo(Long userId, String email, String nickName, String userIdentifier) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.userIdentifier = userIdentifier;
    }
}
