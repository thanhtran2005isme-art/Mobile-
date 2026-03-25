package com.example.banquanao.ui.home.model;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * Một ô danh mục nhanh: icon tròn + nhãn.
 */
public class CategoryItem {

    @DrawableRes
    public final int iconRes;

    @ColorRes
    public final int bgColorRes;

    public final String label;

    public CategoryItem(@DrawableRes int iconRes,
                        @ColorRes int bgColorRes,
                        String label) {
        this.iconRes = iconRes;
        this.bgColorRes = bgColorRes;
        this.label = label;
    }
}
