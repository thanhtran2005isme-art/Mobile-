package com.example.banquanao.ui.category.model;

import androidx.annotation.DrawableRes;

/**
 * Một ô tròn trong lưới "Lựa chọn cho bạn".
 */
public class CategoryGridItem {

    @DrawableRes
    public final int imageRes;

    public final String label;

    public CategoryGridItem(@DrawableRes int imageRes, String label) {
        this.imageRes = imageRes;
        this.label = label;
    }
}
