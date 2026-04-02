package com.example.banquanao.ui.category.model;

import java.util.List;

/**
 * Sub-category trong sidebar trái + danh sách item con hiển thị bên phải.
 */
public class SubCategory {
    public final String label;
    public final List<CategoryGridItem> items;

    public SubCategory(String label, List<CategoryGridItem> items) {
        this.label = label;
        this.items = items;
    }
}
