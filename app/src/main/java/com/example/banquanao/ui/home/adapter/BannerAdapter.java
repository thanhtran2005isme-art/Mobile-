package com.example.banquanao.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.BannerItem;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerVH> {

    private final List<BannerItem> items;

    public BannerAdapter(List<BannerItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new BannerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class BannerVH extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView title;
        private final TextView subtitle;
        private final TextView cta;

        BannerVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bannerImage);
            title = itemView.findViewById(R.id.bannerTitle);
            subtitle = itemView.findViewById(R.id.bannerSubtitle);
            cta = itemView.findViewById(R.id.bannerCta);
        }

        void bind(BannerItem item) {
            image.setImageResource(item.imageRes);
            title.setText(item.title);
            subtitle.setText(item.subtitle);
            cta.setText(item.cta);
        }
    }
}
