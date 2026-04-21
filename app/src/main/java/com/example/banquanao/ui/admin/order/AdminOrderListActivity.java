package com.example.banquanao.ui.admin.order;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminOrder;
import com.example.banquanao.data.admin.OrderRepository;
import com.example.banquanao.util.MoneyFormatter;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderListActivity extends AppCompatActivity {

    private OrderRepository repo;

    private TextView chipPending, chipConfirmed, chipShipping, chipDone, chipCancelled;
    private TextView orderCount;
    private LinearLayout container;
    private EditText searchInput;

    /** null = chưa chọn → mặc định show "Chờ xác nhận" giống ảnh. */
    private AdminOrder.Status filterStatus = AdminOrder.Status.PENDING;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orderAdminRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        repo = OrderRepository.getInstance();
        container = findViewById(R.id.orderContainer);
        orderCount = findViewById(R.id.orderCount);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        setupChips();
        setupSearch();
        renderList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderList();
    }

    // ----- Chips ---------------------------------------------------------

    private void setupChips() {
        chipPending = findViewById(R.id.chipPending);
        chipConfirmed = findViewById(R.id.chipConfirmed);
        chipShipping = findViewById(R.id.chipShipping);
        chipDone = findViewById(R.id.chipDone);
        chipCancelled = findViewById(R.id.chipCancelled);

        chipPending.setOnClickListener(v -> selectFilter(AdminOrder.Status.PENDING));
        chipConfirmed.setOnClickListener(v -> selectFilter(AdminOrder.Status.CONFIRMED));
        chipShipping.setOnClickListener(v -> selectFilter(AdminOrder.Status.SHIPPING));
        chipDone.setOnClickListener(v -> selectFilter(AdminOrder.Status.DONE));
        chipCancelled.setOnClickListener(v -> selectFilter(AdminOrder.Status.CANCELLED));

        refreshChipStyles();
    }

    private void selectFilter(@NonNull AdminOrder.Status status) {
        filterStatus = status;
        refreshChipStyles();
        renderList();
    }

    private void refreshChipStyles() {
        styleChip(chipPending, filterStatus == AdminOrder.Status.PENDING);
        styleChip(chipConfirmed, filterStatus == AdminOrder.Status.CONFIRMED);
        styleChip(chipShipping, filterStatus == AdminOrder.Status.SHIPPING);
        styleChip(chipDone, filterStatus == AdminOrder.Status.DONE);
        styleChip(chipCancelled, filterStatus == AdminOrder.Status.CANCELLED);
    }

    private void styleChip(@NonNull TextView chip, boolean active) {
        chip.setBackgroundResource(active
                ? R.drawable.bg_admin_filter_chip_active
                : R.drawable.bg_admin_filter_chip);
        chip.setTextColor(getColor(active
                ? R.color.profile_orange
                : R.color.text_secondary));
        chip.setTypeface(active ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    // ----- Search --------------------------------------------------------

    private void setupSearch() {
        searchInput = findViewById(R.id.orderSearch);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) {
                keyword = s.toString().trim().toLowerCase();
                renderList();
            }
        });
    }

    // ----- Render --------------------------------------------------------

    private void renderList() {
        List<AdminOrder> all = repo.getAll();
        List<AdminOrder> filtered = new ArrayList<>();
        for (AdminOrder o : all) {
            if (filterStatus != null && o.status != filterStatus) continue;
            if (!keyword.isEmpty()) {
                String hay = (o.code + " " + nullSafe(o.customerName) + " "
                        + nullSafe(o.phone)).toLowerCase();
                if (!hay.contains(keyword)) continue;
            }
            filtered.add(o);
        }

        // Counter
        if (filterStatus != null) {
            orderCount.setText(getString(R.string.admin_order_count,
                    filtered.size(),
                    getString(OrderStatusUi.label(filterStatus)).toLowerCase()));
        } else {
            orderCount.setText(getString(R.string.admin_order_count_all, filtered.size()));
        }

        // List
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (AdminOrder o : filtered) {
            View row = inflater.inflate(R.layout.item_admin_order, container, false);
            bindOrder(row, o);
            container.addView(row);
        }
    }

    private void bindOrder(@NonNull View row, @NonNull AdminOrder o) {
        ((TextView) row.findViewById(R.id.orderCode)).setText(o.code);
        ((TextView) row.findViewById(R.id.orderCustomer))
                .setText(nullSafe(o.customerName) + " • " + nullSafe(o.phone));
        ((TextView) row.findViewById(R.id.orderDate))
                .setText(getString(R.string.admin_order_date, nullSafe(o.date)));
        ((TextView) row.findViewById(R.id.orderTotal)).setText(MoneyFormatter.vnd(o.total));

        TextView statusBadge = row.findViewById(R.id.orderStatus);
        statusBadge.setText(OrderStatusUi.label(o.status));
        statusBadge.setBackgroundResource(OrderStatusUi.bg(o.status));
        statusBadge.setTextColor(getColor(OrderStatusUi.textColor(o.status)));

        row.findViewById(R.id.btnUpdate).setOnClickListener(v -> openUpdateSheet(o));
    }

    private void openUpdateSheet(@NonNull AdminOrder order) {
        OrderStatusBottomSheet.show(getSupportFragmentManager(), order.code, order.status,
                newStatus -> {
                    repo.updateStatus(order.code, newStatus);
                    renderList();
                });
    }

    // ----- Helpers -------------------------------------------------------

    @NonNull
    private static String nullSafe(@Nullable String s) {
        return s == null ? "" : s;
    }
}
