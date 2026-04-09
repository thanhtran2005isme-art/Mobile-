package com.example.banquanao.data.auth;

/**
 * Thông tin user đang đăng nhập.
 */
public class User {
    public final String name;
    public final String phone;
    public final String role;

    public User(String name, String phone, String role) {
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}
