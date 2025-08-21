package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;

@Entity
public class UserInfo extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String nickName;

}
