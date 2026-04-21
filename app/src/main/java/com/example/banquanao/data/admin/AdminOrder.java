package com.example.banquanao.data.admin;

/**
 * Đơn hàng trong trang quản lý admin.
 */
public class AdminOrder {

    public enum Status {
        PENDING,    // Chờ xác nhận
        CONFIRMED,  // Đã xác nhận
        SHIPPING,   // Đang giao
        DONE,       // Hoàn thành
        CANCELLED   // Đã hủy
    }

    public final String code;
    public String customerName;
    public String phone;
    public String date;
    public long total;
    public Status status;

    public AdminOrder(String code) {
        this.code = code;
        this.status = Status.PENDING;
    }
}
