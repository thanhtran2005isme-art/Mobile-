package com.example.banquanao.ui.category.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.category.model.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubVH> {

    public interface OnSubSelected {
        void onSelected(int position);
    }

    private final List<SubCategory> items;
    private int selectedIndex = 0;
    private final OnSubSelected listener;

    public SubCategoryAdapter(List<SubCategory> items, OnSubSelected listener) {
        this.items = items;
        this.listener = listener;
    }

    public void replaceItems(List<SubCategory> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        this.selectedIndex = 0;
        notifyDataSetChanged();
    }

    public List<SubCategory> getCurrentList() {
        return items;
    }

    public void setSelected(int index) {
        if (index == selectedIndex) return;
        int prev = selectedIndex;
        selectedIndex = index;
        notifyItemChanged(prev);
        notifyItemChanged(selectedIndex);
    }

    @NonNull
    @Override
    public SubVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory, parent, false);
        return new SubVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubVH holder, int position) {
        holder.bind(items.get(position), position == selectedIndex);
        holder.itemView.setOnClickListener(v -> {
            int index = holder.getBindingAdapterPosition();
            if (index == RecyclerView.NO_POSITION) return;
            setSelected(index);
            if (listener != null) listener.onSelected(index);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SubVH extends RecyclerView.ViewHolder {
        private final View root;
        private final TextView label;

        SubVH(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.subRoot);
            label = itemView.findViewById(R.id.subLabel);
        }

        void bind(SubCategory item, boolean active) {
            label.setText(item.label);
            root.setBackgroundResource(active
                    ? R.drawable.bg_subcategory_active
                    : R.drawable.bg_subcategory_default);
            label.setTypeface(active ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            label.setTextColor(ContextCompat.getColor(itemView.getContext(),
                    active ? R.color.text_primary : R.color.text_secondary));
        }
    }
}
