package com.example.banquanao.ui.profile;

import android.content.Intent;
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
import com.example.banquanao.data.auth.SessionManager;
import com.example.banquanao.data.auth.User;
import com.example.banquanao.ui.auth.LoginActivity;

public class ProfileFragment extends Fragment {

    private SessionManager session;

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
        session = new SessionManager(requireContext());

        bindUserInfo(view);
        bindOrders(view);
        bindUtilities(view);
        bindSupport(view);
        bindAdminGate(view);
        bindLogout(view);
    }

    // ----- User info -----------------------------------------------------

    private void bindUserInfo(@NonNull View root) {
        User user = session.getCurrentUser();
        TextView name = root.findViewById(R.id.profileName);
        TextView meta = root.findViewById(R.id.profileMeta);
        if (user != null) {
            name.setText(user.name);
            meta.setText(getString(R.string.profile_phone_role_format, user.phone, user.role));
        }
    }

    // ----- Đơn mua -------------------------------------------------------

    private void bindOrders(@NonNull View root) {
        TextView orderHistory = root.findViewById(R.id.orderHistory);
        orderHistory.setText(getString(R.string.profile_orders_link, 2));

        bindQuickAction(root, R.id.orderPending, R.drawable.ic_order_pending,
                R.string.profile_pending_confirm, "2");
        bindQuickAction(root, R.id.orderPickup, R.drawable.ic_order_pickup,
                R.string.profile_pending_pickup, null);
        bindQuickAction(root, R.id.orderDelivery, R.drawable.ic_order_truck,
                R.string.profile_pending_delivery, null);
        bindQuickAction(root, R.id.orderReview, R.drawable.ic_order_review,
                R.string.profile_review, null);
    }

    private void bindUtilities(@NonNull View root) {
        bindQuickAction(root, R.id.utilVoucher, R.drawable.ic_voucher,
                R.string.profile_voucher, null);
        bindQuickAction(root, R.id.utilPoints, R.drawable.ic_coin,
                R.string.profile_points, null);
        bindQuickAction(root, R.id.utilWallet, R.drawable.ic_wallet,
                R.string.profile_wallet, null);
    }

    private void bindSupport(@NonNull View root) {
        bindSupportRow(root, R.id.supportHelp, R.drawable.ic_help,
                R.string.profile_help_center);
        bindSupportRow(root, R.id.supportNotification, R.drawable.ic_notification,
                R.string.profile_my_notification);
        bindSupportRow(root, R.id.supportCare, R.drawable.ic_headset,
                R.string.profile_customer_care);
        bindSupportRow(root, R.id.supportPolicy, R.drawable.ic_shield,
                R.string.profile_purchase_policy);
    }

    /** Chỉ hiển thị section Quản trị khi user hiện tại là admin. */
    private void bindAdminGate(@NonNull View root) {
        View adminSection = root.findViewById(R.id.sectionAdmin);
        if (adminSection != null) {
            adminSection.setVisibility(session.isCurrentUserAdmin()
                    ? View.VISIBLE : View.GONE);
        }
    }

    private void bindLogout(@NonNull View root) {
        root.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    // ----- Helpers -------------------------------------------------------

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
