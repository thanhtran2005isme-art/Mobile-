package com.example.banquanao.ui.home.model;

import androidx.annotation.DrawableRes;

/**
 * Card lớn trong section "Bộ Sưu Tập".
 */
public class CollectionItem {

    @DrawableRes
    public final int imageRes;

    public final String title;

    public CollectionItem(@DrawableRes int imageRes, String title) {
        this.imageRes = imageRes;
        this.title = title;
    }
}
