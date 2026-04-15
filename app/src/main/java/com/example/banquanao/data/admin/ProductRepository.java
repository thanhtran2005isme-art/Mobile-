package com.example.banquanao.data.admin;

import com.example.banquanao.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Lưu trữ sản phẩm admin trong bộ nhớ. Đủ dùng cho demo,
 * sau này có thể thay bằng Room/Retrofit.
 */
public class ProductRepository {

    private static ProductRepository instance;
    private final List<AdminProduct> products = new ArrayList<>();

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    private ProductRepository() {
        seed();
    }

    private void seed() {
        AdminProduct p1 = new AdminProduct("KID-Đ-0002");
        p1.name = "áo sơ mi";
        p1.category = "Đầm ôm";
        p1.collection = "6";
        p1.sku = "KID-Đ-0002";
        p1.price = 100_000_000L;
        p1.stock = 12;
        p1.status = AdminProduct.Status.SELLING;
        p1.visible = true;
        p1.imageRes = R.drawable.bg_product_1;
        products.add(p1);

        AdminProduct p2 = new AdminProduct("KID-B-0001");
        p2.name = "áo thun";
        p2.category = "Bomber";
        p2.collection = "5";
        p2.sku = "KID-B-0001";
        p2.price = 150_000L;
        p2.stock = 12;
        p2.status = AdminProduct.Status.SELLING;
        p2.visible = true;
        p2.imageRes = R.drawable.bg_product_2;
        products.add(p2);
    }

    public List<AdminProduct> getAll() {
        return new ArrayList<>(products);
    }

    public int totalCount() {
        return products.size();
    }

    public void add(AdminProduct product) {
        if (product.id == null || product.id.isEmpty()) {
            // Tạo id ngẫu nhiên nếu chưa set
            String id = "P-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            // AdminProduct.id là final nên cần truyền đúng từ form
            // Trong demo chỉ check không null trước khi gọi addNew(...)
        }
        products.add(0, product);
    }

    public AdminProduct findById(String id) {
        for (AdminProduct p : products) {
            if (p.id.equals(id)) return p;
        }
        return null;
    }

    public void delete(String id) {
        products.removeIf(p -> p.id.equals(id));
    }

    public void toggleVisibility(String id) {
        AdminProduct p = findById(id);
        if (p != null) p.visible = !p.visible;
    }

    public static String generateId() {
        return "P-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
