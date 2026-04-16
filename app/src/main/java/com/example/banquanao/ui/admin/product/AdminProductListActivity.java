package com.example.banquanao.ui.admin.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminProduct;
import com.example.banquanao.data.admin.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminProductListActivity extends AppCompatActivity
        implements AdminProductAdapter.OnProductAction {

    private ProductRepository repo;
    private AdminProductAdapter adapter;

    private TextView chipAll, chipSelling, chipSoldOut, chipDraft;
    private TextView chipCategory, chipCollection;
    private TextView productCount;
    private EditText searchInput;

    private AdminProduct.Status filterStatus = null; // null = all
    private String searchKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productAdminRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        repo = ProductRepository.getInstance();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        setupChips();
        setupSearch();
        setupList();
        setupFab();
        applyFilters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload sau khi vừa thêm/sửa sản phẩm
        applyFilters();
    }

    // ----- Chips ---------------------------------------------------------

    private void setupChips() {
        chipAll = findViewById(R.id.chipAll);
        chipSelling = findViewById(R.id.chipSelling);
        chipSoldOut = findViewById(R.id.chipSoldOut);
        chipDraft = findViewById(R.id.chipDraft);
        chipCategory = findViewById(R.id.chipCategory);
        chipCollection = findViewById(R.id.chipCollection);

        chipAll.setText(R.string.admin_filter_all);
        chipSelling.setText(R.string.admin_filter_selling);
        chipSoldOut.setText(R.string.admin_filter_sold_out);
        chipDraft.setText(R.string.admin_filter_draft);
        chipCategory.setText(getString(R.string.admin_filter_category,
                getString(R.string.admin_filter_value_all)));
        chipCollection.setText(getString(R.string.admin_filter_collection,
                getString(R.string.admin_filter_value_all)));

        chipAll.setOnClickListener(v -> updateStatus(null));
        chipSelling.setOnClickListener(v -> updateStatus(AdminProduct.Status.SELLING));
        chipSoldOut.setOnClickListener(v -> updateStatus(AdminProduct.Status.SOLD_OUT));
        chipDraft.setOnClickListener(v -> updateStatus(AdminProduct.Status.DRAFT));

        chipCategory.setOnClickListener(v -> Toast.makeText(this,
                "Bộ lọc danh mục — sắp ra mắt", Toast.LENGTH_SHORT).show());
        chipCollection.setOnClickListener(v -> Toast.makeText(this,
                "Bộ lọc bộ sưu tập — sắp ra mắt", Toast.LENGTH_SHORT).show());

        refreshChipStyles();
    }

    private void updateStatus(AdminProduct.Status status) {
        filterStatus = status;
        refreshChipStyles();
        applyFilters();
    }

    private void refreshChipStyles() {
        styleChip(chipAll, filterStatus == null);
        styleChip(chipSelling, filterStatus == AdminProduct.Status.SELLING);
        styleChip(chipSoldOut, filterStatus == AdminProduct.Status.SOLD_OUT);
        styleChip(chipDraft, filterStatus == AdminProduct.Status.DRAFT);
    }

    private void styleChip(TextView chip, boolean active) {
        chip.setBackgroundResource(active
                ? R.drawable.bg_admin_filter_chip_active
                : R.drawable.bg_admin_filter_chip);
        chip.setTextColor(getColor(active
                ? R.color.profile_orange
                : R.color.text_secondary));
        chip.setTypeface(active
                ? android.graphics.Typeface.DEFAULT_BOLD
                : android.graphics.Typeface.DEFAULT);
    }

    // ----- Search --------------------------------------------------------

    private void setupSearch() {
        searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                searchKeyword = s.toString().trim().toLowerCase();
                applyFilters();
            }
        });
    }

    // ----- List ----------------------------------------------------------

    private void setupList() {
        productCount = findViewById(R.id.productCount);
        adapter = new AdminProductAdapter(this);
        RecyclerView recycler = findViewById(R.id.productList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    private void applyFilters() {
        List<AdminProduct> all = repo.getAll();
        List<AdminProduct> filtered = new ArrayList<>();
        for (AdminProduct p : all) {
            if (filterStatus != null && p.status != filterStatus) continue;
            if (!searchKeyword.isEmpty()) {
                String hay = (p.name + " " + (p.sku == null ? "" : p.sku)).toLowerCase();
                if (!hay.contains(searchKeyword)) continue;
            }
            filtered.add(p);
        }
        adapter.replace(filtered);
        productCount.setText(getString(R.string.admin_product_count,
                filtered.size(), all.size()));
    }

    // ----- FAB -----------------------------------------------------------

    private void setupFab() {
        findViewById(R.id.fabAddProduct).setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));
    }

    // ----- Actions -------------------------------------------------------

    @Override
    public void onEdit(AdminProduct product) {
        Toast.makeText(this, "Sửa sản phẩm — sắp ra mắt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggleVisibility(AdminProduct product) {
        repo.toggleVisibility(product.id);
        applyFilters();
    }

    @Override
    public void onDelete(AdminProduct product) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa \"" + product.name + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    repo.delete(product.id);
                    applyFilters();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
