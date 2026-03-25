package com.example.banquanao.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.HashtagItem;

import java.util.List;

public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.HashtagVH> {

    private final List<HashtagItem> items;

    public HashtagAdapter(List<HashtagItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HashtagVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hashtag, parent, false);
        return new HashtagVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HashtagVH extends RecyclerView.ViewHolder {
        private final TextView label;

        HashtagVH(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.chipLabel);
        }

        void bind(HashtagItem item) {
            label.setText(item.label);
        }
    }
}
