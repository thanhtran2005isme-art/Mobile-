package com.example.banquanao.ui.admin.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminOrder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Bottom sheet cho phép admin chọn trạng thái mới cho đơn hàng.
 */
public class OrderStatusBottomSheet extends BottomSheetDialogFragment {

    public interface OnStatusPicked {
        void onPicked(@NonNull AdminOrder.Status status);
    }

    private static final String TAG = "OrderStatusBottomSheet";
    private static final String ARG_CODE = "code";
    private static final String ARG_CURRENT = "current";

    private OnStatusPicked listener;

    public static void show(@NonNull FragmentManager fm,
                            @NonNull String code,
                            @NonNull AdminOrder.Status current,
                            @NonNull OnStatusPicked listener) {
        OrderStatusBottomSheet sheet = new OrderStatusBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CODE, code);
        args.putString(ARG_CURRENT, current.name());
        sheet.setArguments(args);
        sheet.listener = listener;
        sheet.show(fm, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = requireArguments();
        String code = args.getString(ARG_CODE, "");
        AdminOrder.Status current = AdminOrder.Status.valueOf(
                args.getString(ARG_CURRENT, AdminOrder.Status.PENDING.name()));

        ((TextView) view.findViewById(R.id.sheetTitle))
                .setText(getString(R.string.admin_order_update_title, code));

        LinearLayout options = view.findViewById(R.id.sheetOptions);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (AdminOrder.Status status : AdminOrder.Status.values()) {
            View row = inflater.inflate(R.layout.item_order_status_option, options, false);
            TextView label = row.findViewById(R.id.optionLabel);
            View check = row.findViewById(R.id.optionCheck);

            label.setText(OrderStatusUi.label(status));
            label.setBackgroundResource(OrderStatusUi.bg(status));
            label.setTextColor(requireContext().getColor(OrderStatusUi.textColor(status)));
            check.setVisibility(status == current ? View.VISIBLE : View.INVISIBLE);

            row.setOnClickListener(v -> {
                if (status != current && listener != null) {
                    listener.onPicked(status);
                    Toast.makeText(requireContext(),
                            R.string.admin_order_update_done, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            });
            options.addView(row);
        }
    }
}
