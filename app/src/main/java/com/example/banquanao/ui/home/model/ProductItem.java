package com.example.banquanao.ui.home.model;

import androidx.annotation.DrawableRes;

/**
 * Một sản phẩm trong section "Gợi Ý Riêng Cho ...".
 */
public class ProductItem {

    @DrawableRes
    public final int imageRes;

    public final String shopHandle;
    public final String price;
    public final String name;
    public final boolean isPreorder;
    public final boolean isLiked;

    public ProductItem(@DrawableRes int imageRes,
                       String shopHandle,
                       String price,
                       String name,
                       boolean isPreorder,
                       boolean isLiked) {
        this.imageRes = imageRes;
        this.shopHandle = shopHandle;
        this.price = price;
        this.name = name;
        this.isPreorder = isPreorder;
        this.isLiked = isLiked;
    }
}
