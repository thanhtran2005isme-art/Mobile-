package com.example.banquanao.ui.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.category.model.CategoryGridItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.GridVH> {

    private final List<CategoryGridItem> items = new ArrayList<>();

    public CategoryGridAdapter(List<CategoryGridItem> initial) {
        items.addAll(initial);
    }

    public void replace(List<CategoryGridItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_grid, parent, false);
        return new GridVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GridVH extends RecyclerView.ViewHolder {
        private final View image;
        private final TextView label;

        GridVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gridImage);
            label = itemView.findViewById(R.id.gridLabel);
        }

        void bind(CategoryGridItem item) {
            image.setBackgroundResource(item.imageRes);
            label.setText(item.label);
        }
    }
}
