package com.example.banquanao.ui.home.model;

import androidx.annotation.DrawableRes;

/**
 * Card shop trong section "Shop Đang Được Yêu Thích".
 */
public class ShopItem {

    @DrawableRes
    public final int coverRes;

    public final String avatarLetter;
    public final String name;
    public final String description;
    public final boolean isTop;

    public ShopItem(@DrawableRes int coverRes,
                    String avatarLetter,
                    String name,
                    String description,
                    boolean isTop) {
        this.coverRes = coverRes;
        this.avatarLetter = avatarLetter;
        this.name = name;
        this.description = description;
        this.isTop = isTop;
    }
}
