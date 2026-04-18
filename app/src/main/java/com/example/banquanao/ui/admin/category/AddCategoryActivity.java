package com.example.banquanao.ui.admin.category;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminCategory;
import com.example.banquanao.data.admin.CategoryRepository;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_PARENT_ID = "extra_parent_id";
    public static final String EXTRA_EDIT_ID = "extra_edit_id";

    private CategoryRepository repo;

    private TextView screenTitle;
    private TextView levelChip1, levelChip2, levelChip3;
    private View parentSection;
    private TextView parentSelector;
    private MaterialSwitch switchVisible;

    private int selectedLevel = 1;
    private String selectedParentId;
    private String editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addCategoryRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        repo = CategoryRepository.getInstance();

        screenTitle = findViewById(R.id.screenTitle);
        levelChip1 = findViewById(R.id.levelChip1);
        levelChip2 = findViewById(R.id.levelChip2);
        levelChip3 = findViewById(R.id.levelChip3);
        parentSection = findViewById(R.id.parentSection);
        parentSelector = findViewById(R.id.parentSelector);
        switchVisible = findViewById(R.id.switchVisible);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btnSave).setOnClickListener(v -> save());

        configField(R.id.fieldName, R.string.add_category_name, R.string.add_category_name_hint);
        configField(R.id.fieldSlug, R.string.add_category_slug, R.string.add_category_slug_hint);
        configField(R.id.fieldOrder, R.string.add_category_order, R.string.add_category_order_hint);
        findField(R.id.fieldOrder).setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        levelChip1.setOnClickListener(v -> selectLevel(1));
        levelChip2.setOnClickListener(v -> selectLevel(2));
        levelChip3.setOnClickListener(v -> selectLevel(3));
        parentSelector.setOnClickListener(v -> openParentPicker());

        applyExtras();
    }

    private void applyExtras() {
        editId = getIntent().getStringExtra(EXTRA_EDIT_ID);
        String parentIdHint = getIntent().getStringExtra(EXTRA_PARENT_ID);

        if (editId != null) {
            // Mode sửa
            screenTitle.setText(R.string.add_category_edit_title);
            AdminCategory existing = repo.findById(editId);
            if (existing != null) {
                findField(R.id.fieldName).setText(existing.name);
                findField(R.id.fieldSlug).setText(existing.slug);
                findField(R.id.fieldOrder).setText(String.valueOf(existing.order));
                switchVisible.setChecked(existing.visible);
                selectLevel(existing.level);
                if (existing.parentId != null) {
                    selectedParentId = existing.parentId;
                    AdminCategory parent = repo.findById(existing.parentId);
                    if (parent != null) parentSelector.setText(parent.name);
                }
            }
        } else if (parentIdHint != null) {
            // Mode "Thêm con" của cha cụ thể
            AdminCategory parent = repo.findById(parentIdHint);
            if (parent != null) {
                selectedParentId = parent.id;
                parentSelector.setText(parent.name);
                selectLevel(Math.min(3, parent.level + 1));
            }
        } else {
            // Mặc định cấp 1
            selectLevel(1);
        }
    }

    // ----- Level chip ----------------------------------------------------

    private void selectLevel(int level) {
        selectedLevel = level;
        styleChip(levelChip1, level == 1);
        styleChip(levelChip2, level == 2);
        styleChip(levelChip3, level == 3);

        // C1 không cần parent, C2/C3 có
        parentSection.setVisibility(level == 1 ? View.GONE : View.VISIBLE);

        // Nếu level đổi, parent cũ có thể không hợp lệ nữa
        if (level == 1) {
            selectedParentId = null;
            parentSelector.setText("");
        } else if (selectedParentId != null) {
            AdminCategory parent = repo.findById(selectedParentId);
            // C2 phải có parent là C1, C3 phải có parent là C2
            if (parent == null || parent.level != level - 1) {
                selectedParentId = null;
                parentSelector.setText(R.string.add_category_parent_none);
            }
        }
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

    // ----- Parent picker -------------------------------------------------

    private void openParentPicker() {
        // Lọc theo level: C2 cần parent C1, C3 cần parent C2
        int requiredLevel = selectedLevel - 1;
        List<AdminCategory> candidates = new ArrayList<>();
        for (AdminCategory c : repo.getPossibleParents()) {
            if (c.level == requiredLevel) candidates.add(c);
        }
        if (candidates.isEmpty()) {
            Toast.makeText(this, "Chưa có danh mục cha phù hợp", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) names[i] = candidates.get(i).name;

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_category_parent)
                .setItems(names, (d, which) -> {
                    AdminCategory picked = candidates.get(which);
                    selectedParentId = picked.id;
                    parentSelector.setText(picked.name);
                })
                .show();
    }

    // ----- Save ----------------------------------------------------------

    private void save() {
        String name = fieldText(R.id.fieldName);
        String slug = fieldText(R.id.fieldSlug);
        String orderText = fieldText(R.id.fieldOrder);

        if (name.isEmpty()) {
            toast(R.string.add_category_error_name);
            return;
        }
        if (slug.isEmpty()) {
            toast(R.string.add_category_error_slug);
            return;
        }
        if (selectedLevel >= 2 && selectedParentId == null) {
            toast(R.string.add_category_error_level);
            return;
        }
        int order = orderText.isEmpty() ? 1 : Integer.parseInt(orderText);

        AdminCategory target;
        if (editId != null) {
            target = repo.findById(editId);
            if (target == null) {
                finish();
                return;
            }
        } else {
            target = new AdminCategory(CategoryRepository.generateId());
            repo.add(target);
        }
        target.name = name;
        target.slug = slug;
        target.level = selectedLevel;
        target.parentId = selectedLevel == 1 ? null : selectedParentId;
        target.order = order;
        target.visible = switchVisible.isChecked();

        Toast.makeText(this, R.string.add_category_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    // ----- Helpers -------------------------------------------------------

    private void configField(@IdRes int includeId,
                             @StringRes int labelRes,
                             @StringRes int hintRes) {
        View root = findViewById(includeId);
        if (root == null) return;
        TextView label = root.findViewById(R.id.fieldLabel);
        EditText input = root.findViewById(R.id.fieldInput);
        label.setText(labelRes);
        input.setHint(hintRes);
    }

    @NonNull
    private EditText findField(@IdRes int includeId) {
        View root = findViewById(includeId);
        return root.findViewById(R.id.fieldInput);
    }

    @NonNull
    private String fieldText(@IdRes int includeId) {
        return findField(includeId).getText().toString().trim();
    }

    private void toast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
