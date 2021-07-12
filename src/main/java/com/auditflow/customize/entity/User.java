package com.auditflow.customize.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2021/7/5 10:31
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String uuid;
    private String username;
}
