package com.example.banquanao.ui.home.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Khoảng cách giữa các ô trong GridLayoutManager với spanCount cho trước.
 * Đảm bảo cột đầu/cuối thẳng mép, các cột giữa có khoảng cách đều.
 */
public class GridSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int spacingPx;
    private final int spanCount;

    public GridSpaceDecoration(int spacingPx, int spanCount) {
        this.spacingPx = spacingPx;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position < 0) return;
        int column = position % spanCount;

        outRect.left = column * spacingPx / spanCount;
        outRect.right = spacingPx - (column + 1) * spacingPx / spanCount;
        if (position >= spanCount) {
            outRect.top = spacingPx;
        }
    }
}
