package com.example.banquanao.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.model.ProductItem;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {

    private final List<ProductItem> items;

    public ProductAdapter(List<ProductItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ProductVH extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView shop;
        private final TextView price;
        private final TextView preorder;
        private final TextView name;
        private final ImageView like;

        ProductVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            shop = itemView.findViewById(R.id.productShop);
            price = itemView.findViewById(R.id.productPrice);
            preorder = itemView.findViewById(R.id.productPreorder);
            name = itemView.findViewById(R.id.productName);
            like = itemView.findViewById(R.id.productLike);
        }

        void bind(ProductItem item) {
            image.setImageResource(item.imageRes);
            shop.setText(item.shopHandle);
            price.setText(item.price);
            name.setText(item.name);
            preorder.setVisibility(item.isPreorder ? View.VISIBLE : View.GONE);
            like.setAlpha(item.isLiked ? 1f : 0.7f);
        }
    }
}
