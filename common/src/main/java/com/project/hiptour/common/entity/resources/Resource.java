package com.project.hiptour.common.entity.resources;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;


/**
 *
 * @apiNote 리소스는, (타입, owner_id, resource_id)로 인덱스를 구성하여, 탐색 시, 타입 및, owner_id 검색한다
 *
 * **/
@Entity
public class Resource extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;
    @Enumerated(EnumType.STRING) // <== 중요
    private ResourceType resourceType;
    private long resourceOwnerId;
    private String originalResourceName;
    private String maskedResourceName;
    @Embedded
    private ResourcePath resourcePath;
    @Embedded
    private ResourceFormat format;
    @Embedded
    private SizeInfo sizeInfo;
}
