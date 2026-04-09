package com.example.banquanao.data.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Quản lý phiên đăng nhập và danh sách user đã đăng ký.
 *
 * Tài khoản admin mặc định: 0906264126 / admin (role = "Quản trị")
 * Các user còn lại có role = "Người dùng".
 */
public class SessionManager {

    private static final String PREFS = "session";
    private static final String KEY_CURRENT_USER = "current_user";
    private static final String KEY_USERS = "registered_users";

    private static final String ADMIN_PHONE = "0906264126";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_NAME = "thanh";
    private static final String ROLE_ADMIN = "Quản trị";
    private static final String ROLE_USER = "Người dùng";

    private final SharedPreferences prefs;

    public SessionManager(@NonNull Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return prefs.contains(KEY_CURRENT_USER);
    }

    /**
     * Đăng nhập. Trả về User nếu thành công, null nếu sai.
     */
    @Nullable
    public User login(@NonNull String phone, @NonNull String password) {
        if (ADMIN_PHONE.equals(phone) && ADMIN_PASSWORD.equals(password)) {
            User u = new User(ADMIN_NAME, ADMIN_PHONE, ROLE_ADMIN);
            saveCurrent(u);
            return u;
        }
        // Tìm trong danh sách đã đăng ký
        Set<String> users = prefs.getStringSet(KEY_USERS, new HashSet<>());
        for (String json : users) {
            try {
                JSONObject obj = new JSONObject(json);
                if (phone.equals(obj.optString("phone"))
                        && password.equals(obj.optString("password"))) {
                    User u = new User(
                            obj.optString("name"),
                            obj.optString("phone"),
                            ROLE_USER);
                    saveCurrent(u);
                    return u;
                }
            } catch (JSONException ignored) { }
        }
        return null;
    }

    /**
     * Đăng ký user mới. Trả về true nếu OK, false nếu phone đã tồn tại.
     */
    public boolean register(@NonNull String name,
                            @NonNull String phone,
                            @NonNull String password) {
        if (ADMIN_PHONE.equals(phone)) return false;

        Set<String> users = new HashSet<>(prefs.getStringSet(KEY_USERS, new HashSet<>()));
        for (String json : users) {
            try {
                JSONObject obj = new JSONObject(json);
                if (phone.equals(obj.optString("phone"))) return false;
            } catch (JSONException ignored) { }
        }

        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("phone", phone);
            obj.put("password", password);
            users.add(obj.toString());
            prefs.edit().putStringSet(KEY_USERS, users).apply();
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Nullable
    public User getCurrentUser() {
        String json = prefs.getString(KEY_CURRENT_USER, null);
        if (json == null) return null;
        try {
            JSONObject obj = new JSONObject(json);
            return new User(
                    obj.optString("name"),
                    obj.optString("phone"),
                    obj.optString("role"));
        } catch (JSONException e) {
            return null;
        }
    }

    public boolean isCurrentUserAdmin() {
        User u = getCurrentUser();
        return u != null && ROLE_ADMIN.equals(u.role);
    }

    public void logout() {
        prefs.edit().remove(KEY_CURRENT_USER).apply();
    }

    private void saveCurrent(@NonNull User user) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", user.name);
            obj.put("phone", user.phone);
            obj.put("role", user.role);
            prefs.edit().putString(KEY_CURRENT_USER, obj.toString()).apply();
        } catch (JSONException ignored) { }
    }
}
