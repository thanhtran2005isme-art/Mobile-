package com.example.banquanao.ui.home.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    private final List<CategoryItem> items;

    public CategoryAdapter(List<CategoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CategoryVH extends RecyclerView.ViewHolder {
        private final FrameLayout iconWrap;
        private final ImageView icon;
        private final TextView label;

        CategoryVH(@NonNull View itemView) {
            super(itemView);
            iconWrap = itemView.findViewById(R.id.iconWrap);
            icon = itemView.findViewById(R.id.iconImage);
            label = itemView.findViewById(R.id.iconLabel);
        }

        void bind(CategoryItem item) {
            icon.setImageResource(item.iconRes);
            label.setText(item.label);
            int tintColor = ContextCompat.getColor(itemView.getContext(), item.bgColorRes);
            iconWrap.setBackgroundTintList(ColorStateList.valueOf(tintColor));
        }
    }
}
