package com.example.banquanao.data.admin;

/**
 * Một node trong cây danh mục, hỗ trợ tối đa 3 cấp.
 * - level 1: cha gốc (không có parentId)
 * - level 2: con của level 1
 * - level 3: con của level 2
 */
public class AdminCategory {

    public final String id;
    public String name;
    public String slug;
    public int level;        // 1, 2, 3
    public int order;        // thứ tự hiển thị
    public boolean visible;
    public String parentId;  // null nếu là cấp 1

    public AdminCategory(String id) {
        this.id = id;
        this.level = 1;
        this.order = 1;
        this.visible = true;
    }
}
