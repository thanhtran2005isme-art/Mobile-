package com.example.banquanao.ui.home.model;

import androidx.annotation.DrawableRes;

/**
 * Card lớn trong section "Bộ Sưu Tập".
 */
public class CollectionItem {

    @DrawableRes
    public final int imageRes;

    public final String title;
    public final String subtitle;

    public CollectionItem(@DrawableRes int imageRes, String title, String subtitle) {
        this.imageRes = imageRes;
        this.title = title;
        this.subtitle = subtitle;
    }
}
