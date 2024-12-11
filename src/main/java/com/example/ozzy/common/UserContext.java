package com.example.ozzy.common;

public class UserContext {

    private static final ThreadLocal<Integer> THREAD_LOCAL_USER_ID = new ThreadLocal<>();

    public static void setUserId(int userId) {
        THREAD_LOCAL_USER_ID.set(userId);
    }

    public static int getUserId() {
        return THREAD_LOCAL_USER_ID.get();
    }

    public static void clear() {
        THREAD_LOCAL_USER_ID.remove();
    }
}

