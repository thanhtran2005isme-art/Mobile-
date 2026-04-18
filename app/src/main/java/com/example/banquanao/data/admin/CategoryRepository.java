package com.example.banquanao.data.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Repository in-memory cho cây danh mục. Cung cấp:
 * - Thao tác CRUD: add, update, delete
 * - Lấy theo phẳng đã sắp xếp DFS để render dễ
 * - Đếm theo cấp
 */
public class CategoryRepository {

    private static CategoryRepository instance;
    private final List<AdminCategory> categories = new ArrayList<>();

    public static CategoryRepository getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }

    private CategoryRepository() {
        seed();
    }

    private void seed() {
        AdminCategory c1 = newRoot("Phụ nữ", "phu-nu", 1);
        AdminCategory c2 = newChild("Váy", "vay", c1.id, 1);
        newChild("Váy cưới", "vay-cuoi", c2.id, 1);
        newChild("Váy cocktail", "vay-cocktail", c2.id, 2);

        newRoot("Đường cong", "duong-cong", 2);
        newRoot("Trẻ em", "tre-em", 3);
        newRoot("Đồ nam", "do-nam", 4);
    }

    // ----- Read ----------------------------------------------------------

    /** Trả về list phẳng theo thứ tự DFS để render cây. */
    @NonNull
    public List<AdminCategory> getFlattenedTree() {
        List<AdminCategory> result = new ArrayList<>();
        for (AdminCategory root : sortedRoots()) {
            walk(root, result);
        }
        return result;
    }

    private void walk(AdminCategory node, List<AdminCategory> out) {
        out.add(node);
        List<AdminCategory> children = childrenOf(node.id);
        children.sort(Comparator.comparingInt(c -> c.order));
        for (AdminCategory child : children) {
            walk(child, out);
        }
    }

    @NonNull
    public List<AdminCategory> sortedRoots() {
        List<AdminCategory> roots = new ArrayList<>();
        for (AdminCategory c : categories) {
            if (c.level == 1) roots.add(c);
        }
        roots.sort(Comparator.comparingInt(c -> c.order));
        return roots;
    }

    @NonNull
    public List<AdminCategory> childrenOf(String parentId) {
        List<AdminCategory> result = new ArrayList<>();
        for (AdminCategory c : categories) {
            if (parentId.equals(c.parentId)) result.add(c);
        }
        return result;
    }

    /** Lấy tất cả category ở cấp 1 và cấp 2 (có thể làm cha). */
    @NonNull
    public List<AdminCategory> getPossibleParents() {
        List<AdminCategory> result = new ArrayList<>();
        for (AdminCategory c : categories) {
            if (c.level <= 2) result.add(c);
        }
        return result;
    }

    @Nullable
    public AdminCategory findById(String id) {
        for (AdminCategory c : categories) {
            if (c.id.equals(id)) return c;
        }
        return null;
    }

    public int countByLevel(int level) {
        int count = 0;
        for (AdminCategory c : categories) {
            if (c.level == level) count++;
        }
        return count;
    }

    public int countSub() {
        return categories.size() - countByLevel(1);
    }

    // ----- Write ---------------------------------------------------------

    public AdminCategory newRoot(String name, String slug, int order) {
        AdminCategory c = new AdminCategory(generateId());
        c.name = name;
        c.slug = slug;
        c.level = 1;
        c.order = order;
        categories.add(c);
        return c;
    }

    public AdminCategory newChild(String name, String slug, String parentId, int order) {
        AdminCategory parent = findById(parentId);
        if (parent == null) {
            throw new IllegalArgumentException("Parent not found: " + parentId);
        }
        AdminCategory c = new AdminCategory(generateId());
        c.name = name;
        c.slug = slug;
        c.level = Math.min(3, parent.level + 1);
        c.parentId = parentId;
        c.order = order;
        categories.add(c);
        return c;
    }

    public void add(@NonNull AdminCategory c) {
        categories.add(c);
    }

    public void delete(@NonNull String id) {
        // xóa con trước (đệ quy)
        List<AdminCategory> children = new ArrayList<>(childrenOf(id));
        for (AdminCategory child : children) delete(child.id);
        categories.removeIf(c -> c.id.equals(id));
    }

    public void toggleVisibility(@NonNull String id) {
        AdminCategory c = findById(id);
        if (c != null) c.visible = !c.visible;
    }

    public static String generateId() {
        return "C-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
