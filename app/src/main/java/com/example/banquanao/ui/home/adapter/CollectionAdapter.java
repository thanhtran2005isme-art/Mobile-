package com.example.banquanao.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.CollectionItem;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionVH> {

    private final List<CollectionItem> items;

    public CollectionAdapter(List<CollectionItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CollectionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection, parent, false);
        return new CollectionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CollectionVH extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView title;

        CollectionVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.collectionImage);
            title = itemView.findViewById(R.id.collectionTitle);
        }

        void bind(CollectionItem item) {
            image.setImageResource(item.imageRes);
            title.setText(item.title);
        }
    }
}
