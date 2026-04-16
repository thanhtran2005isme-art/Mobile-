package com.example.banquanao.ui.admin.product;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductVH> {

    public interface OnProductAction {
        void onEdit(AdminProduct product);
        void onToggleVisibility(AdminProduct product);
        void onDelete(AdminProduct product);
    }

    private final List<AdminProduct> items = new ArrayList<>();
    private final OnProductAction listener;

    public AdminProductAdapter(OnProductAction listener) {
        this.listener = listener;
    }

    public void replace(List<AdminProduct> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_product, parent, false);
        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ProductVH extends RecyclerView.ViewHolder {
        private final ImageView thumb;
        private final TextView status;
        private final TextView visibility;
        private final TextView name;
        private final TextView category;
        private final TextView collection;
        private final TextView sku;
        private final TextView price;
        private final TextView stock;
        private final TextView actionEdit;
        private final TextView actionToggle;
        private final TextView actionDelete;

        ProductVH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.productThumb);
            status = itemView.findViewById(R.id.productStatus);
            visibility = itemView.findViewById(R.id.productVisibility);
            name = itemView.findViewById(R.id.productName);
            category = itemView.findViewById(R.id.productCategory);
            collection = itemView.findViewById(R.id.productCollection);
            sku = itemView.findViewById(R.id.productSku);
            price = itemView.findViewById(R.id.productPrice);
            stock = itemView.findViewById(R.id.productStock);
            actionEdit = itemView.findViewById(R.id.actionEdit);
            actionToggle = itemView.findViewById(R.id.actionToggle);
            actionDelete = itemView.findViewById(R.id.actionDelete);
        }

        void bind(@NonNull AdminProduct p, @NonNull OnProductAction listener) {
            if (p.imageRes != 0) thumb.setImageResource(p.imageRes);

            // Status badge
            switch (p.status) {
                case SELLING:
                    status.setText(itemView.getContext().getString(R.string.admin_filter_selling));
                    status.setBackgroundResource(R.drawable.bg_admin_status_green);
                    status.setTextColor(itemView.getContext().getColor(R.color.admin_status_green));
                    break;
                case SOLD_OUT:
                    status.setText(itemView.getContext().getString(R.string.admin_filter_sold_out));
                    status.setBackgroundResource(R.drawable.bg_admin_status_red);
                    status.setTextColor(itemView.getContext().getColor(R.color.admin_status_red));
                    break;
                case DRAFT:
                    status.setText(itemView.getContext().getString(R.string.admin_filter_draft));
                    status.setBackgroundResource(R.drawable.bg_admin_status_red);
                    status.setTextColor(itemView.getContext().getColor(R.color.admin_status_amber));
                    break;
            }

            if (p.visible) {
                visibility.setText(R.string.admin_product_status_visible);
                visibility.setBackgroundResource(R.drawable.bg_admin_status_green);
                visibility.setTextColor(itemView.getContext().getColor(R.color.admin_status_green));
                actionToggle.setText(R.string.admin_product_action_hide);
            } else {
                visibility.setText(R.string.admin_product_status_hidden);
                visibility.setBackgroundResource(R.drawable.bg_admin_status_red);
                visibility.setTextColor(itemView.getContext().getColor(R.color.admin_status_red));
                actionToggle.setText(R.string.admin_product_action_show);
            }

            name.setText(TextUtils.isEmpty(p.name) ? "—" : p.name);
            category.setText(itemView.getContext().getString(R.string.admin_product_field_category,
                    nullSafe(p.category)));
            collection.setText(itemView.getContext().getString(R.string.admin_product_field_collection,
                    nullSafe(p.collection)));
            sku.setText(itemView.getContext().getString(R.string.admin_product_field_sku,
                    nullSafe(p.sku)));
            price.setText(formatPrice(p.price));
            stock.setText(itemView.getContext().getString(R.string.admin_product_field_stock, p.stock));

            actionEdit.setOnClickListener(v -> listener.onEdit(p));
            actionToggle.setOnClickListener(v -> listener.onToggleVisibility(p));
            actionDelete.setOnClickListener(v -> listener.onDelete(p));
        }

        private static String nullSafe(String s) {
            return s == null ? "—" : s;
        }

        private static String formatPrice(long price) {
            // 100000 -> "100000đ", 100000000 -> "100000000đ"
            return NumberFormat.getInstance(new Locale("vi", "VN")).format(price)
                    .replace(",", "")
                    .replace(".", "") + "đ";
        }
    }
}
