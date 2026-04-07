package com.example.banquanao.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.banquanao.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Đơn mua
        TextView orderHistory = view.findViewById(R.id.orderHistory);
        orderHistory.setText(getString(R.string.profile_orders_link, 2));

        bindQuickAction(view, R.id.orderPending, R.drawable.ic_order_pending,
                R.string.profile_pending_confirm, "2");
        bindQuickAction(view, R.id.orderPickup, R.drawable.ic_order_pickup,
                R.string.profile_pending_pickup, null);
        bindQuickAction(view, R.id.orderDelivery, R.drawable.ic_order_truck,
                R.string.profile_pending_delivery, null);
        bindQuickAction(view, R.id.orderReview, R.drawable.ic_order_review,
                R.string.profile_review, null);

        // Tiện ích
        bindQuickAction(view, R.id.utilVoucher, R.drawable.ic_voucher,
                R.string.profile_voucher, null);
        bindQuickAction(view, R.id.utilPoints, R.drawable.ic_coin,
                R.string.profile_points, null);
        bindQuickAction(view, R.id.utilWallet, R.drawable.ic_wallet,
                R.string.profile_wallet, null);

        // Hỗ trợ
        bindSupportRow(view, R.id.supportHelp, R.drawable.ic_help,
                R.string.profile_help_center);
        bindSupportRow(view, R.id.supportNotification, R.drawable.ic_notification,
                R.string.profile_my_notification);
        bindSupportRow(view, R.id.supportCare, R.drawable.ic_headset,
                R.string.profile_customer_care);
        bindSupportRow(view, R.id.supportPolicy, R.drawable.ic_shield,
                R.string.profile_purchase_policy);
    }

    /**
     * Đổ icon + label (+ badge) cho 1 ô quick action đã include trong layout.
     */
    private void bindQuickAction(@NonNull View root,
                                 @IdRes int includeId,
                                 @DrawableRes int iconRes,
                                 @StringRes int labelRes,
                                 @Nullable String badge) {
        View item = root.findViewById(includeId);
        ImageView icon = item.findViewById(R.id.quickIcon);
        TextView label = item.findViewById(R.id.quickLabel);
        TextView badgeView = item.findViewById(R.id.quickBadge);

        icon.setImageResource(iconRes);
        label.setText(labelRes);
        if (badge != null) {
            badgeView.setText(badge);
            badgeView.setVisibility(View.VISIBLE);
        } else {
            badgeView.setVisibility(View.GONE);
        }
    }

    private void bindSupportRow(@NonNull View root,
                                @IdRes int includeId,
                                @DrawableRes int iconRes,
                                @StringRes int labelRes) {
        View item = root.findViewById(includeId);
        ImageView icon = item.findViewById(R.id.supportIcon);
        TextView label = item.findViewById(R.id.supportLabel);
        icon.setImageResource(iconRes);
        label.setText(labelRes);
    }
}
