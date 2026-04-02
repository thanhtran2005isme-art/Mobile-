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
import com.example.banquanao.ui.category.model.TopTab;

import java.util.List;

public class TopTabAdapter extends RecyclerView.Adapter<TopTabAdapter.TopTabVH> {

    public interface OnTabSelected {
        void onSelected(int position);
    }

    private final List<TopTab> items;
    private int selectedIndex = 0;
    private final OnTabSelected listener;

    public TopTabAdapter(List<TopTab> items, OnTabSelected listener) {
        this.items = items;
        this.listener = listener;
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
    public TopTabVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_tab, parent, false);
        return new TopTabVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTabVH holder, int position) {
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

    static class TopTabVH extends RecyclerView.ViewHolder {
        private final TextView label;
        private final View indicator;

        TopTabVH(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.tabLabel);
            indicator = itemView.findViewById(R.id.tabIndicator);
        }

        void bind(TopTab item, boolean active) {
            label.setText(item.label);
            label.setTypeface(active ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            label.setTextColor(ContextCompat.getColor(itemView.getContext(),
                    active ? R.color.text_primary : R.color.text_secondary));
            indicator.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
