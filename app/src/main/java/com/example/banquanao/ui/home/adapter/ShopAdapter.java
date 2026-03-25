package com.example.banquanao.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.ShopItem;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopVH> {

    private final List<ShopItem> items;

    public ShopAdapter(List<ShopItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ShopVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ShopVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShopVH extends RecyclerView.ViewHolder {
        private final ImageView cover;
        private final TextView avatarLetter;
        private final TextView name;
        private final TextView desc;
        private final TextView topBadge;

        ShopVH(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.shopCover);
            avatarLetter = itemView.findViewById(R.id.shopAvatarLetter);
            name = itemView.findViewById(R.id.shopName);
            desc = itemView.findViewById(R.id.shopDesc);
            topBadge = itemView.findViewById(R.id.shopTopBadge);
        }

        void bind(ShopItem item) {
            cover.setImageResource(item.coverRes);
            avatarLetter.setText(item.avatarLetter);
            name.setText(item.name);
            desc.setText(item.description);
            topBadge.setVisibility(item.isTop ? View.VISIBLE : View.GONE);
        }
    }
}
