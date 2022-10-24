package com.yfdl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleBySelectVo {
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;
}
