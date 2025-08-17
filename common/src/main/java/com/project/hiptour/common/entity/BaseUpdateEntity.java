package com.project.hiptour.common.entity;

import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public abstract class BaseUpdateEntity extends BaseTimeEntity{
    @Column(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "create_user")
    protected UserInfo createUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "update_user")
    protected UserInfo updateUser;


}
