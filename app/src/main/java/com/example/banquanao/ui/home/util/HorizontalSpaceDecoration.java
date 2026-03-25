package com.example.banquanao.ui.home.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Đặt khoảng cách giữa các item trong RecyclerView ngang/dọc.
 */
public class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int spacePx;
    private final boolean horizontal;

    public HorizontalSpaceDecoration(int spacePx, boolean horizontal) {
        this.spacePx = spacePx;
        this.horizontal = horizontal;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int last = state.getItemCount() - 1;
        if (horizontal) {
            outRect.right = position == last ? 0 : spacePx;
        } else {
            outRect.bottom = position == last ? 0 : spacePx;
        }
    }
}
