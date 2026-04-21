package com.example.banquanao.data.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository in-memory cho đơn hàng admin.
 */
public class OrderRepository {

    private static OrderRepository instance;
    private final List<AdminOrder> orders = new ArrayList<>();

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    private OrderRepository() {
        seed();
    }

    private void seed() {
        AdminOrder o1 = new AdminOrder("KK260429105918");
        o1.customerName = "thanh";
        o1.phone = "0906264126";
        o1.date = "29/04/2026";
        o1.total = 100_016_500L;
        o1.status = AdminOrder.Status.PENDING;
        orders.add(o1);

        AdminOrder o2 = new AdminOrder("KK260429105850");
        o2.customerName = "thanh";
        o2.phone = "0906264126";
        o2.date = "29/04/2026";
        o2.total = 100_016_500L;
        o2.status = AdminOrder.Status.PENDING;
        orders.add(o2);

        AdminOrder o3 = new AdminOrder("KK260428084215");
        o3.customerName = "lan";
        o3.phone = "0987654321";
        o3.date = "28/04/2026";
        o3.total = 350_000L;
        o3.status = AdminOrder.Status.CONFIRMED;
        orders.add(o3);

        AdminOrder o4 = new AdminOrder("KK260427093200");
        o4.customerName = "minh";
        o4.phone = "0912345678";
        o4.date = "27/04/2026";
        o4.total = 1_250_000L;
        o4.status = AdminOrder.Status.SHIPPING;
        orders.add(o4);
    }

    @NonNull
    public List<AdminOrder> getAll() {
        return new ArrayList<>(orders);
    }

    public int countByStatus(@NonNull AdminOrder.Status status) {
        int n = 0;
        for (AdminOrder o : orders) if (o.status == status) n++;
        return n;
    }

    @Nullable
    public AdminOrder findByCode(@NonNull String code) {
        for (AdminOrder o : orders) if (o.code.equals(code)) return o;
        return null;
    }

    public void updateStatus(@NonNull String code, @NonNull AdminOrder.Status newStatus) {
        AdminOrder o = findByCode(code);
        if (o != null) o.status = newStatus;
    }
}
