package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.RoleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Data
public class TokenTemplate {
    private final long userId;
    private final List<Integer> roleIds;
}
