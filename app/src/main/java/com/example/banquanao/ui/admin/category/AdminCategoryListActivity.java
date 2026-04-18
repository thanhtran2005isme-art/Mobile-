package com.example.banquanao.ui.admin.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminCategory;
import com.example.banquanao.data.admin.CategoryRepository;

import java.util.List;

public class AdminCategoryListActivity extends AppCompatActivity {

    private CategoryRepository repo;
    private LinearLayout container;
    private TextView statsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.categoryAdminRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        repo = CategoryRepository.getInstance();
        container = findViewById(R.id.categoryContainer);
        statsView = findViewById(R.id.categoryStats);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddCategory).setOnClickListener(v ->
                openAddCategory(null, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderTree();
    }

    // ----- Render --------------------------------------------------------

    private void renderTree() {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        List<AdminCategory> nodes = repo.getFlattenedTree();
        int density = (int) getResources().getDisplayMetrics().density;
        int indent = (int) getResources().getDimension(R.dimen.admin_category_indent);

        for (AdminCategory node : nodes) {
            View item = inflater.inflate(R.layout.item_admin_category, container, false);
            bindItem(item, node);

            // Indent theo level: level 1 = 0, level 2 = 1*indent, level 3 = 2*indent
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) item.getLayoutParams();
            if (lp != null) {
                lp.leftMargin = (node.level - 1) * indent;
                item.setLayoutParams(lp);
            }
            container.addView(item);
        }

        statsView.setText(getString(R.string.admin_category_stats,
                repo.countByLevel(1), repo.countSub()));
    }

    private void bindItem(@NonNull View item, @NonNull AdminCategory node) {
        TextView badge = item.findViewById(R.id.categoryLevelBadge);
        TextView name = item.findViewById(R.id.categoryName);
        TextView meta = item.findViewById(R.id.categoryMeta);
        TextView status = item.findViewById(R.id.categoryStatus);
        TextView actionAddChild = item.findViewById(R.id.actionAddChild);
        TextView actionEdit = item.findViewById(R.id.actionEdit);
        TextView actionToggle = item.findViewById(R.id.actionToggle);
        TextView actionDelete = item.findViewById(R.id.actionDelete);

        badge.setText(getString(R.string.admin_category_level_format, node.level));
        name.setText(node.name);
        meta.setText(getString(R.string.admin_category_meta,
                node.slug == null ? "" : node.slug, node.order));

        if (node.visible) {
            status.setText(R.string.admin_category_visible);
            status.setBackgroundResource(R.drawable.bg_admin_status_green);
            status.setTextColor(getColor(R.color.admin_status_green));
            actionToggle.setText(R.string.admin_category_action_hide);
        } else {
            status.setText(R.string.admin_category_hidden);
            status.setBackgroundResource(R.drawable.bg_admin_status_red);
            status.setTextColor(getColor(R.color.admin_status_red));
            actionToggle.setText(R.string.admin_category_action_show);
        }

        // C3 không thể thêm con nữa
        actionAddChild.setVisibility(node.level < 3 ? View.VISIBLE : View.GONE);

        actionAddChild.setOnClickListener(v ->
                openAddCategory(node.id, null));
        actionEdit.setOnClickListener(v ->
                openAddCategory(null, node.id));
        actionToggle.setOnClickListener(v -> {
            repo.toggleVisibility(node.id);
            renderTree();
        });
        actionDelete.setOnClickListener(v -> confirmDelete(node));
    }

    private void confirmDelete(@NonNull AdminCategory node) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.admin_category_action_delete)
                .setMessage(getString(R.string.admin_category_delete_confirm, node.name))
                .setPositiveButton(R.string.admin_category_action_delete, (d, w) -> {
                    repo.delete(node.id);
                    renderTree();
                })
                .setNegativeButton(R.string.add_product_cancel, null)
                .show();
    }

    private void openAddCategory(String parentIdToCreateChild, String editId) {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        if (parentIdToCreateChild != null) {
            intent.putExtra(AddCategoryActivity.EXTRA_PARENT_ID, parentIdToCreateChild);
        }
        if (editId != null) {
            intent.putExtra(AddCategoryActivity.EXTRA_EDIT_ID, editId);
        }
        startActivity(intent);
    }
}
