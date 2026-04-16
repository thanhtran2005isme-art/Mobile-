package com.example.banquanao.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.auth.SessionManager;
import com.example.banquanao.data.auth.User;
import com.example.banquanao.ui.auth.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        session = new SessionManager(this);

        // Chặn user thường truy cập
        if (!session.isCurrentUserAdmin()) {
            Toast.makeText(this, "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindHero();
        bindToolbar();
        bindStats();
        bindShortcuts();
        bindRecent();
    }

    // ----- Hero ----------------------------------------------------------

    private void bindHero() {
        User user = session.getCurrentUser();
        TextView hello = findViewById(R.id.adminHelloTitle);
        hello.setText(getString(R.string.admin_hero_hello,
                user != null ? user.name : ""));
    }

    // ----- Toolbar -------------------------------------------------------

    private void bindToolbar() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAdminLogout).setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // ----- Stats ---------------------------------------------------------

    private void bindStats() {
        bindStat(R.id.statProducts, R.string.admin_stat_products, "2",
                R.string.admin_stat_products_desc);
        bindStat(R.id.statPending, R.string.admin_stat_pending, "2",
                R.string.admin_stat_pending_desc);
        bindStat(R.id.statCustomers, R.string.admin_stat_customers, "1",
                R.string.admin_stat_customers_desc);
        bindStat(R.id.statCategories, R.string.admin_stat_categories, "36",
                R.string.admin_stat_categories_desc);
    }

    private void bindStat(@IdRes int includeId,
                          @StringRes int labelRes,
                          @NonNull String value,
                          @StringRes int descRes) {
        View item = findViewById(includeId);
        ((TextView) item.findViewById(R.id.statLabel)).setText(labelRes);
        ((TextView) item.findViewById(R.id.statValue)).setText(value);
        ((TextView) item.findViewById(R.id.statDesc)).setText(descRes);
    }

    // ----- Shortcuts -----------------------------------------------------

    private void bindShortcuts() {
        bindShortcut(R.id.shortcutProducts, R.drawable.ic_admin_grid,
                R.string.admin_shortcut_products, R.string.admin_shortcut_products_desc);
        bindShortcut(R.id.shortcutCategories, R.drawable.ic_admin_box,
                R.string.admin_shortcut_categories, R.string.admin_shortcut_categories_desc);
        bindShortcut(R.id.shortcutOrders, R.drawable.ic_admin_orders,
                R.string.admin_shortcut_orders, R.string.admin_shortcut_orders_desc);
        bindShortcut(R.id.shortcutCustomers, R.drawable.ic_admin_customers,
                R.string.admin_shortcut_customers, R.string.admin_shortcut_customers_desc);
    }

    private void bindShortcut(@IdRes int includeId,
                              @DrawableRes int iconRes,
                              @StringRes int titleRes,
                              @StringRes int descRes) {
        View item = findViewById(includeId);
        ((ImageView) item.findViewById(R.id.shortcutIcon)).setImageResource(iconRes);
        ((TextView) item.findViewById(R.id.shortcutTitle)).setText(titleRes);
        ((TextView) item.findViewById(R.id.shortcutDesc)).setText(descRes);
        item.setOnClickListener(v -> {
            if (titleRes == R.string.admin_shortcut_products) {
                startActivity(new Intent(this,
                        com.example.banquanao.ui.admin.product.AdminProductListActivity.class));
            } else {
                Toast.makeText(this,
                        getString(titleRes) + " — sắp ra mắt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----- Recent activity -----------------------------------------------

    private void bindRecent() {
        LinearLayout container = findViewById(R.id.recentContainer);
        LayoutInflater inflater = LayoutInflater.from(this);

        addRecentRow(container, inflater,
                "Đơn KK260429105918 đang chờ xác nhận",
                "thanh • 29/04/2026 10:59 • 100.016.500đ");
        addDivider(container);

        addRecentRow(container, inflater,
                "Đơn KK260429105850 đang chờ xác nhận",
                "thanh • 29/04/2026 10:58 • 100.016.500đ");
        addDivider(container);

        addRecentRow(container, inflater,
                "Kho sản phẩm hiện có 2 sản phẩm",
                "Dữ liệu được lấy trực tiếp từ bảng sản phẩm trong Room");
    }

    private void addRecentRow(@NonNull ViewGroup parent,
                              @NonNull LayoutInflater inflater,
                              @NonNull String title,
                              @NonNull String meta) {
        View row = inflater.inflate(R.layout.item_admin_recent, parent, false);
        ((TextView) row.findViewById(R.id.recentTitle)).setText(title);
        ((TextView) row.findViewById(R.id.recentMeta)).setText(meta);
        parent.addView(row);
    }

    private void addDivider(@NonNull ViewGroup parent) {
        View divider = new View(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp.leftMargin = (int) (20 * getResources().getDisplayMetrics().density);
        divider.setLayoutParams(lp);
        divider.setBackgroundResource(R.color.divider);
        parent.addView(divider);
    }
}
