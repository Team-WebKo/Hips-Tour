package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;

@Entity
public class RoleTaskInfo extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleTaskId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleInfo roleInfo;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskInfo taskInfo;


}
