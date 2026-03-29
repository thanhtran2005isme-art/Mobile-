package com.example.banquanao.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.banquanao.R;
import com.example.banquanao.ui.home.adapter.BannerAdapter;
import com.example.banquanao.ui.home.adapter.CategoryAdapter;
import com.example.banquanao.ui.home.adapter.CollectionAdapter;
import com.example.banquanao.ui.home.adapter.HashtagAdapter;
import com.example.banquanao.ui.home.adapter.ProductAdapter;
import com.example.banquanao.ui.home.adapter.ShopAdapter;
import com.example.banquanao.ui.home.model.BannerItem;
import com.example.banquanao.ui.home.model.CategoryItem;
import com.example.banquanao.ui.home.model.CollectionItem;
import com.example.banquanao.ui.home.model.HashtagItem;
import com.example.banquanao.ui.home.model.ProductItem;
import com.example.banquanao.ui.home.model.ShopItem;
import com.example.banquanao.ui.home.util.GridSpaceDecoration;
import com.example.banquanao.ui.home.util.HorizontalSpaceDecoration;

import java.util.ArrayList;
import java.util.Arrays;
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
        setupCategories(view);
        setupHashtags(view);
        setupShops(view);
        setupCollections(view);
        setupRecommend(view);
    }

    // ----- Banner ---------------------------------------------------------

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

    // ----- Categories -----------------------------------------------------

    private void setupCategories(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.categoryRecycler);
        GridLayoutManager glm = new GridLayoutManager(
                requireContext(), 2,
                GridLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(glm);
        recycler.setAdapter(new CategoryAdapter(buildCategoryData()));
        recycler.setNestedScrollingEnabled(false);
    }

    private List<CategoryItem> buildCategoryData() {
        return Arrays.asList(
                new CategoryItem(R.drawable.ic_cat_gia_tot, R.color.cat_red, getString(R.string.cat_gia_tot)),
                new CategoryItem(R.drawable.ic_cat_freeship, R.color.cat_orange, getString(R.string.cat_freeship)),
                new CategoryItem(R.drawable.ic_cat_new, R.color.cat_pink, getString(R.string.cat_moi)),
                new CategoryItem(R.drawable.ic_cat_sale, R.color.cat_red, getString(R.string.cat_uu_dai)),
                new CategoryItem(R.drawable.ic_cat_store, R.color.cat_orange, getString(R.string.cat_cua_hang)),
                new CategoryItem(R.drawable.ic_cat_star, R.color.cat_yellow, getString(R.string.cat_danh_gia)),
                new CategoryItem(R.drawable.ic_cat_blook, R.color.cat_pink, getString(R.string.cat_blook)),
                new CategoryItem(R.drawable.ic_cat_air, R.color.cat_green, getString(R.string.cat_bidu_air)),
                new CategoryItem(R.drawable.ic_cat_shirt, R.color.cat_purple, getString(R.string.cat_ao)),
                new CategoryItem(R.drawable.ic_cat_man, R.color.cat_blue, getString(R.string.cat_do_nam))
        );
    }

    // ----- Hashtag chips --------------------------------------------------

    private void setupHashtags(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.hashtagRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.addItemDecoration(new HorizontalSpaceDecoration(dp(8), true));
        recycler.setAdapter(new HashtagAdapter(buildHashtagData()));
        recycler.setNestedScrollingEnabled(false);
    }

    private List<HashtagItem> buildHashtagData() {
        return Arrays.asList(
                new HashtagItem(getString(R.string.tag_bidubestie)),
                new HashtagItem(getString(R.string.tag_newin)),
                new HashtagItem(getString(R.string.tag_trending))
        );
    }

    // ----- Shops yêu thích -----------------------------------------------

    private void setupShops(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.shopRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.addItemDecoration(new HorizontalSpaceDecoration(dp(12), true));
        recycler.setAdapter(new ShopAdapter(buildShopData()));
        recycler.setNestedScrollingEnabled(false);
    }

    private List<ShopItem> buildShopData() {
        return Arrays.asList(
                new ShopItem(R.drawable.bg_shop_1, "E", "ESTHER ST.", "Thương hiệu xuất xứ …", true),
                new ShopItem(R.drawable.bg_shop_2, "X", "Xexymix", "Bán phối athleisure", false),
                new ShopItem(R.drawable.bg_shop_3, "L", "LIBÉ", "Thời trang nữ tinh tế", false)
        );
    }

    // ----- Bộ sưu tập -----------------------------------------------------

    private void setupCollections(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.collectionRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.addItemDecoration(new HorizontalSpaceDecoration(dp(12), true));
        recycler.setAdapter(new CollectionAdapter(buildCollectionData()));
        recycler.setNestedScrollingEnabled(false);
    }

    private List<CollectionItem> buildCollectionData() {
        return Arrays.asList(
                new CollectionItem(R.drawable.bg_collection_1, "Beautiful lies"),
                new CollectionItem(R.drawable.bg_collection_2, "Hai mặt không bao giờ"),
                new CollectionItem(R.drawable.bg_collection_3, "Tối giản")
        );
    }

    // ----- Gợi ý riêng (Grid 2 cột) --------------------------------------

    private void setupRecommend(@NonNull View root) {
        TextView title = root.findViewById(R.id.recommendTitle);
        title.setText(getString(R.string.section_recommend, getString(R.string.default_username)));

        RecyclerView recycler = root.findViewById(R.id.productRecycler);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recycler.addItemDecoration(new GridSpaceDecoration(dp(12), 2));
        recycler.setAdapter(new ProductAdapter(buildProductData()));
        recycler.setNestedScrollingEnabled(false);
    }

    private List<ProductItem> buildProductData() {
        return Arrays.asList(
                new ProductItem(R.drawable.bg_product_1, "ksksn", "100.000.000đ", "áo sơ mi", true, false),
                new ProductItem(R.drawable.bg_product_2, "jdksdb", "150.000đ", "áo thun", true, true),
                new ProductItem(R.drawable.bg_product_3, "store_x", "390.000đ", "quần jean", false, false),
                new ProductItem(R.drawable.bg_product_4, "trendy", "550.000đ", "áo khoác", false, false)
        );
    }

    // ----- Helpers --------------------------------------------------------

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
