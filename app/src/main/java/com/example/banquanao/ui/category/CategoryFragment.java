package com.example.banquanao.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banquanao.R;
import com.example.banquanao.ui.category.adapter.CategoryGridAdapter;
import com.example.banquanao.ui.category.adapter.SubCategoryAdapter;
import com.example.banquanao.ui.category.adapter.TopTabAdapter;
import com.example.banquanao.ui.category.model.CategoryGridItem;
import com.example.banquanao.ui.category.model.SubCategory;
import com.example.banquanao.ui.category.model.TopTab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {

    private SubCategoryAdapter subAdapter;
    private CategoryGridAdapter gridAdapter;

    /** Dữ liệu sub-category cho từng top tab. */
    private final Map<String, List<SubCategory>> subsByTopTab = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildData();
        setupTopTabs(view);
        setupSidebar(view);
        setupGrid(view);
    }

    // ----- Data -----------------------------------------------------------

    private void buildData() {
        subsByTopTab.put(getString(R.string.cat_tab_all), buildSubsAll());
        subsByTopTab.put(getString(R.string.cat_tab_women), buildSubsWomen());
        subsByTopTab.put(getString(R.string.cat_tab_curve), buildSubsAll());
        subsByTopTab.put(getString(R.string.cat_tab_kids), buildSubsAll());
        subsByTopTab.put(getString(R.string.cat_tab_men), buildSubsAll());
        subsByTopTab.put(getString(R.string.cat_tab_accessory), buildSubsAll());
        subsByTopTab.put(getString(R.string.cat_tab_beauty), buildSubsAll());
    }

    private List<SubCategory> buildSubsWomen() {
        List<SubCategory> list = new ArrayList<>();

        list.add(new SubCategory(getString(R.string.cat_sub_dress), Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_blue, getString(R.string.cat_item_wedding_dress)),
                new CategoryGridItem(R.drawable.bg_circle_purple, getString(R.string.cat_item_cocktail)),
                new CategoryGridItem(R.drawable.bg_circle_pink, getString(R.string.cat_item_evening))
        )));

        list.add(new SubCategory(getString(R.string.cat_sub_clothes), Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_orange, getString(R.string.cat_item_maxi)),
                new CategoryGridItem(R.drawable.bg_circle_green, getString(R.string.cat_item_mini)),
                new CategoryGridItem(R.drawable.bg_circle_blue, getString(R.string.cat_item_midi))
        )));

        list.add(new SubCategory(getString(R.string.cat_sub_wedding), Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_pink, getString(R.string.cat_item_wedding_dress)),
                new CategoryGridItem(R.drawable.bg_circle_purple, getString(R.string.cat_item_evening))
        )));

        list.add(new SubCategory(getString(R.string.cat_sub_underwear), Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_pink, "Đồ lót"),
                new CategoryGridItem(R.drawable.bg_circle_purple, "Pijama")
        )));

        list.add(new SubCategory(getString(R.string.cat_sub_workwear), Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_blue, "Sơ mi"),
                new CategoryGridItem(R.drawable.bg_circle_orange, "Vest"),
                new CategoryGridItem(R.drawable.bg_circle_green, "Quần tây")
        )));

        return list;
    }

    private List<SubCategory> buildSubsAll() {
        return Collections.singletonList(new SubCategory("Tất cả", Arrays.asList(
                new CategoryGridItem(R.drawable.bg_circle_blue, "Áo"),
                new CategoryGridItem(R.drawable.bg_circle_pink, "Quần"),
                new CategoryGridItem(R.drawable.bg_circle_purple, "Váy"),
                new CategoryGridItem(R.drawable.bg_circle_orange, "Phụ kiện"),
                new CategoryGridItem(R.drawable.bg_circle_green, "Giày dép"),
                new CategoryGridItem(R.drawable.bg_circle_blue, "Túi xách")
        )));
    }

    // ----- Top tabs -------------------------------------------------------

    private void setupTopTabs(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.topTabRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<TopTab> tabs = Arrays.asList(
                new TopTab(getString(R.string.cat_tab_all)),
                new TopTab(getString(R.string.cat_tab_women)),
                new TopTab(getString(R.string.cat_tab_curve)),
                new TopTab(getString(R.string.cat_tab_kids)),
                new TopTab(getString(R.string.cat_tab_men)),
                new TopTab(getString(R.string.cat_tab_accessory)),
                new TopTab(getString(R.string.cat_tab_beauty))
        );

        TopTabAdapter adapter = new TopTabAdapter(tabs, position -> {
            String key = tabs.get(position).label;
            List<SubCategory> subs = subsByTopTab.get(key);
            if (subs == null || subs.isEmpty()) return;
            subAdapter.replaceItems(subs);
            gridAdapter.replace(subs.get(0).items);
        });
        recycler.setAdapter(adapter);

        // Mặc định active "Phụ nữ" (index 1)
        adapter.setSelected(1);
    }

    // ----- Sidebar --------------------------------------------------------

    private void setupSidebar(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.subCategoryRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<SubCategory> initial = subsByTopTab.get(getString(R.string.cat_tab_women));
        subAdapter = new SubCategoryAdapter(new ArrayList<>(initial), position -> {
            if (gridAdapter == null) return;
            // Lấy danh sách hiện tại đang hiển thị để render lại grid
            List<SubCategory> current = subAdapter.getCurrentList();
            if (position >= 0 && position < current.size()) {
                gridAdapter.replace(current.get(position).items);
            }
        });
        recycler.setAdapter(subAdapter);
    }

    // ----- Grid -----------------------------------------------------------

    private void setupGrid(@NonNull View root) {
        RecyclerView recycler = root.findViewById(R.id.categoryGridRecycler);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        List<SubCategory> initial = subsByTopTab.get(getString(R.string.cat_tab_women));
        gridAdapter = new CategoryGridAdapter(initial.get(0).items);
        recycler.setAdapter(gridAdapter);
    }
}
