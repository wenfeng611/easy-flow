package com.auditflow.customize.context;

import com.auditflow.customize.entity.User;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class UserInfoThreadLocalContext {

    private static final ThreadLocal<User> user = new InheritableThreadLocal<>();


    public static User user() {
        return user.get();
    }

    public static User init_user() {
        User user = new User();
        user.setUsername("testUser");
        return user;
    }

    public static void clear_user() {
        user.remove();
    }


}
