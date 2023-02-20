package com.wf.flow.context;

import com.wf.flow.entity.User;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class UserInfoThreadLocalContext {

    private static final ThreadLocal<User> user = new InheritableThreadLocal<>();


    public static User user() {
        return user.get();
    }

    public static User init_user() {
        User userTest = new User();
        userTest.setUsername("System");
        user.set(userTest);
        return userTest;
    }

    public static void clear_user() {
        user.remove();
    }


}
