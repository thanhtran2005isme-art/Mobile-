package com.example.banquanao.data.admin;

import androidx.annotation.DrawableRes;

/**
 * Sản phẩm trong trang quản lý của admin.
 */
public class AdminProduct {

    public enum Status { SELLING, SOLD_OUT, DRAFT }

    public final String id;
    public String name;
    public String shortDesc;
    public String fullDesc;
    public String material;
    public String style;
    public String brand;
    public String pattern;
    public String season;
    public String body;
    public String gender;
    public String origin;
    public String sku;
    public long price;
    public int stock;
    public String category;
    public String collection;
    public Status status;
    public boolean visible;

    @DrawableRes
    public int imageRes;

    public AdminProduct(String id) {
        this.id = id;
        this.status = Status.SELLING;
        this.visible = true;
    }
}
