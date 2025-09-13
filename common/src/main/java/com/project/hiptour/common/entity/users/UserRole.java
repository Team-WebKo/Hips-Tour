package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class UserRole extends BaseUpdateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleInfo roleInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @Builder
    public UserRole(Long userRoleId, RoleInfo roleInfo, UserInfo userInfo) {
        this.userRoleId = userRoleId;
        this.roleInfo = roleInfo;
        this.userInfo = userInfo;
    }
}
