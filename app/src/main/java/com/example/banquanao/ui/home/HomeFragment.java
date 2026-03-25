package com.example.banquanao.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.adapter.BannerAdapter;
import com.example.banquanao.ui.home.model.BannerItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final long AUTO_SCROLL_DELAY_MS = 4000L;

    private ViewPager2 bannerPager;
    private LinearLayout bannerIndicator;
    private final Handler autoScrollHandler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBanner(view);
    }

    private void setupBanner(@NonNull View root) {
        bannerPager = root.findViewById(R.id.bannerPager);
        bannerIndicator = root.findViewById(R.id.bannerIndicator);

        List<BannerItem> banners = buildBannerData();
        BannerAdapter adapter = new BannerAdapter(banners);
        bannerPager.setAdapter(adapter);
        bannerPager.setOffscreenPageLimit(1);

        buildIndicators(banners.size());
        updateIndicator(0);

        bannerPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicator(position);
                restartAutoScroll();
            }
        });

        startAutoScroll();
    }

    private List<BannerItem> buildBannerData() {
        List<BannerItem> list = new ArrayList<>();
        list.add(new BannerItem(
                R.drawable.bg_banner_placeholder,
                getString(R.string.banner_title),
                getString(R.string.banner_subtitle),
                getString(R.string.banner_cta)));
        list.add(new BannerItem(
                R.drawable.bg_banner_2,
                "SUMMER SALE",
                "Giảm tới 50% toàn bộ BST",
                "Mua ngay"));
        list.add(new BannerItem(
                R.drawable.bg_banner_3,
                "NEW ARRIVAL",
                "Bộ sưu tập mới đã có mặt",
                "Khám phá"));
        return list;
    }

    private void buildIndicators(int count) {
        bannerIndicator.removeAllViews();
        int margin = dp(3);
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(requireContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(lp);
            dot.setImageResource(R.drawable.dot_inactive);
            bannerIndicator.addView(dot);
        }
    }

    private void updateIndicator(int activeIndex) {
        for (int i = 0; i < bannerIndicator.getChildCount(); i++) {
            ImageView dot = (ImageView) bannerIndicator.getChildAt(i);
            dot.setImageResource(i == activeIndex ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }

    private void startAutoScroll() {
        if (autoScrollRunnable != null) return;
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerPager == null || bannerPager.getAdapter() == null) return;
                int next = (bannerPager.getCurrentItem() + 1) % bannerPager.getAdapter().getItemCount();
                bannerPager.setCurrentItem(next, true);
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY_MS);
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY_MS);
    }

    private void restartAutoScroll() {
        if (autoScrollRunnable == null) return;
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY_MS);
    }

    private void stopAutoScroll() {
        if (autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
            autoScrollRunnable = null;
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerPager != null && bannerPager.getAdapter() != null) {
            startAutoScroll();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoScroll();
    }
}
