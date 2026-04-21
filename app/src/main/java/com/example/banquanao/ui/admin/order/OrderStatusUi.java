package com.example.banquanao.ui.admin.order;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminOrder;

/**
 * Map AdminOrder.Status sang label, màu chữ và background pill.
 */
public final class OrderStatusUi {

    private OrderStatusUi() { }

    @StringRes
    public static int label(@NonNull AdminOrder.Status status) {
        switch (status) {
            case PENDING: return R.string.admin_order_status_pending;
            case CONFIRMED: return R.string.admin_order_status_confirmed;
            case SHIPPING: return R.string.admin_order_status_shipping;
            case DONE: return R.string.admin_order_status_done;
            case CANCELLED: return R.string.admin_order_status_cancelled;
        }
        return R.string.admin_order_status_pending;
    }

    @DrawableRes
    public static int bg(@NonNull AdminOrder.Status status) {
        switch (status) {
            case PENDING: return R.drawable.bg_order_status_pending;
            case CONFIRMED: return R.drawable.bg_order_status_confirmed;
            case SHIPPING: return R.drawable.bg_order_status_shipping;
            case DONE: return R.drawable.bg_order_status_done;
            case CANCELLED: return R.drawable.bg_order_status_cancelled;
        }
        return R.drawable.bg_order_status_pending;
    }

    @ColorRes
    public static int textColor(@NonNull AdminOrder.Status status) {
        switch (status) {
            case PENDING: return R.color.order_pending;
            case CONFIRMED: return R.color.order_confirmed;
            case SHIPPING: return R.color.order_shipping;
            case DONE: return R.color.order_done;
            case CANCELLED: return R.color.order_cancelled;
        }
        return R.color.order_pending;
    }
}
