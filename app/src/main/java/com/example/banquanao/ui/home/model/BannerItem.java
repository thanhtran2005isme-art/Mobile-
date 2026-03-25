package com.example.banquanao.ui.home.model;

import androidx.annotation.DrawableRes;

/**
 * Đại diện cho một slide trên banner trang chủ.
 * Hiện đang dùng ảnh placeholder dạng drawable, sau này có thể thay bằng URL.
 */
public class BannerItem {

    @DrawableRes
    public final int imageRes;
    public final String title;
    public final String subtitle;
    public final String cta;

    public BannerItem(@DrawableRes int imageRes, String title, String subtitle, String cta) {
        this.imageRes = imageRes;
        this.title = title;
        this.subtitle = subtitle;
        this.cta = cta;
    }
}
