package com.example.banquanao.ui.admin.product;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.admin.AdminProduct;
import com.example.banquanao.data.admin.ProductRepository;
import com.google.android.material.materialswitch.MaterialSwitch;

public class AddProductActivity extends AppCompatActivity {

    private static final int TOTAL_STEPS = 5;

    private ViewFlipper flipper;
    private TextView stepBadge, stepTitle, stepDesc;
    private LinearLayout stepDotsRow;
    private TextView btnFooterLeft, btnFooterRight;
    private TextView statusSelling, statusSoldOut, statusDraft;
    private MaterialSwitch switchVisible;

    private int currentStep = 0;
    private AdminProduct.Status pickedStatus = AdminProduct.Status.SELLING;

    private final AdminProduct draft = new AdminProduct(ProductRepository.generateId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addProductRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        flipper = findViewById(R.id.stepFlipper);
        stepBadge = findViewById(R.id.stepBadge);
        stepTitle = findViewById(R.id.stepTitle);
        stepDesc = findViewById(R.id.stepDesc);
        stepDotsRow = findViewById(R.id.stepDotsRow);
        btnFooterLeft = findViewById(R.id.btnFooterLeft);
        btnFooterRight = findViewById(R.id.btnFooterRight);
        statusSelling = findViewById(R.id.statusSelling);
        statusSoldOut = findViewById(R.id.statusSoldOut);
        statusDraft = findViewById(R.id.statusDraft);
        switchVisible = findViewById(R.id.switchVisible);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        bindStep1();
        bindStep2();
        bindStep4();
        bindStep5();

        btnFooterLeft.setOnClickListener(v -> onLeftClick());
        btnFooterRight.setOnClickListener(v -> onRightClick());

        buildDots();
        showStep(0);
    }

    // ----- Stepper UI -----------------------------------------------------

    private void buildDots() {
        stepDotsRow.removeAllViews();
        int dotSize = (int) getResources().getDimension(R.dimen.admin_step_dot_size);
        int density = (int) getResources().getDisplayMetrics().density;

        for (int i = 0; i < TOTAL_STEPS; i++) {
            // dot
            TextView dot = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotSize, dotSize);
            dot.setLayoutParams(lp);
            dot.setGravity(android.view.Gravity.CENTER);
            dot.setText(String.valueOf(i + 1));
            dot.setTextSize(14);
            dot.setTextColor(getColor(R.color.text_secondary));
            dot.setBackgroundResource(R.drawable.bg_step_dot);
            dot.setTag("dot_" + i);
            stepDotsRow.addView(dot);

            // line giữa các dot (trừ dot cuối)
            if (i < TOTAL_STEPS - 1) {
                View line = new View(this);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        0, 2 * density, 1f);
                llp.setMargins(2 * density, 0, 2 * density, 0);
                line.setLayoutParams(llp);
                line.setBackgroundColor(getColor(R.color.profile_card_stroke));
                stepDotsRow.addView(line);
            }
        }
    }

    private void updateDots() {
        for (int i = 0; i < TOTAL_STEPS; i++) {
            View dot = stepDotsRow.findViewWithTag("dot_" + i);
            if (!(dot instanceof TextView)) continue;
            TextView tv = (TextView) dot;
            boolean active = (i == currentStep);
            tv.setBackgroundResource(active
                    ? R.drawable.bg_step_dot_active
                    : R.drawable.bg_step_dot);
            tv.setTextColor(getColor(active ? R.color.white : R.color.text_secondary));
            tv.setTypeface(active ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        }
    }

    private void showStep(int step) {
        currentStep = step;
        flipper.setDisplayedChild(step);
        stepBadge.setText(getString(R.string.add_product_step_format, step + 1, TOTAL_STEPS));

        switch (step) {
            case 0:
                stepTitle.setText(R.string.add_product_step1);
                stepDesc.setText(R.string.add_product_step1_desc);
                break;
            case 1:
                stepTitle.setText(R.string.add_product_step2);
                stepDesc.setText(R.string.add_product_step2_desc);
                break;
            case 2:
                stepTitle.setText(R.string.add_product_step3);
                stepDesc.setText(R.string.add_product_step3_desc);
                break;
            case 3:
                stepTitle.setText(R.string.add_product_step4);
                stepDesc.setText(R.string.add_product_step4_desc);
                break;
            case 4:
                stepTitle.setText(R.string.add_product_step5);
                stepDesc.setText(R.string.add_product_step5_desc);
                break;
        }

        // Footer label
        btnFooterLeft.setText(step == 0 ? R.string.add_product_cancel : R.string.add_product_back);
        btnFooterRight.setText(step == TOTAL_STEPS - 1
                ? R.string.add_product_save : R.string.add_product_next);

        updateDots();
    }

    // ----- Step 1: Thông tin chính ---------------------------------------

    private void bindStep1() {
        configField(R.id.fieldName, R.string.add_product_name, R.string.add_product_name);
        configField(R.id.fieldShortDesc, R.string.add_product_short_desc,
                R.string.add_product_short_desc_hint);
        configField(R.id.fieldFullDesc, R.string.add_product_full_desc,
                R.string.add_product_full_desc_hint);
        configField(R.id.fieldMaterial, R.string.add_product_material,
                R.string.add_product_material_hint);
        configField(R.id.fieldStyle, R.string.add_product_style,
                R.string.add_product_style_hint);
        configField(R.id.fieldBrand, R.string.add_product_brand,
                R.string.add_product_brand_hint);
        configField(R.id.fieldPattern, R.string.add_product_pattern,
                R.string.add_product_pattern_hint);
        configField(R.id.fieldSeason, R.string.add_product_season,
                R.string.add_product_season_hint);
        configField(R.id.fieldBody, R.string.add_product_body,
                R.string.add_product_body_hint);
        configField(R.id.fieldGender, R.string.add_product_gender,
                R.string.add_product_gender_hint);
        configField(R.id.fieldOrigin, R.string.add_product_origin,
                R.string.add_product_origin_hint);
    }

    // ----- Step 2 --------------------------------------------------------

    private void bindStep2() {
        configField(R.id.fieldSku, R.string.add_product_sku, R.string.add_product_sku_hint);
        configField(R.id.fieldPrice, R.string.add_product_price, R.string.add_product_price_hint);
        EditText price = findField(R.id.fieldPrice);
        price.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        configField(R.id.fieldStock, R.string.add_product_stock, R.string.add_product_stock_hint);
        EditText stock = findField(R.id.fieldStock);
        stock.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    }

    // ----- Step 4 --------------------------------------------------------

    private void bindStep4() {
        configField(R.id.fieldCategory, R.string.add_product_category,
                R.string.add_product_category_hint);
        configField(R.id.fieldCollection, R.string.add_product_collection,
                R.string.add_product_collection_hint);
    }

    // ----- Step 5 --------------------------------------------------------

    private void bindStep5() {
        statusSelling.setOnClickListener(v -> setStatus(AdminProduct.Status.SELLING));
        statusSoldOut.setOnClickListener(v -> setStatus(AdminProduct.Status.SOLD_OUT));
        statusDraft.setOnClickListener(v -> setStatus(AdminProduct.Status.DRAFT));
        setStatus(AdminProduct.Status.SELLING);
    }

    private void setStatus(AdminProduct.Status status) {
        pickedStatus = status;
        styleStatus(statusSelling, status == AdminProduct.Status.SELLING);
        styleStatus(statusSoldOut, status == AdminProduct.Status.SOLD_OUT);
        styleStatus(statusDraft, status == AdminProduct.Status.DRAFT);
    }

    private void styleStatus(TextView chip, boolean active) {
        chip.setBackgroundResource(active
                ? R.drawable.bg_admin_filter_chip_active
                : R.drawable.bg_admin_filter_chip);
        chip.setTextColor(getColor(active
                ? R.color.profile_orange
                : R.color.text_secondary));
        chip.setTypeface(active ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    // ----- Field helpers -------------------------------------------------

    private void configField(@IdRes int includeId,
                             @StringRes int labelRes,
                             @StringRes int hintRes) {
        View root = findViewById(includeId);
        if (root == null) return;
        TextView label = root.findViewById(R.id.fieldLabel);
        EditText input = root.findViewById(R.id.fieldInput);
        label.setText(labelRes);
        input.setHint(hintRes);
    }

    @NonNull
    private EditText findField(@IdRes int includeId) {
        View root = findViewById(includeId);
        return root.findViewById(R.id.fieldInput);
    }

    @NonNull
    private String fieldText(@IdRes int includeId) {
        return findField(includeId).getText().toString().trim();
    }

    // ----- Footer flow ---------------------------------------------------

    private void onLeftClick() {
        if (currentStep == 0) {
            finish();
        } else {
            showStep(currentStep - 1);
        }
    }

    private void onRightClick() {
        if (!validateCurrentStep()) return;

        if (currentStep < TOTAL_STEPS - 1) {
            showStep(currentStep + 1);
        } else {
            saveProduct();
        }
    }

    private boolean validateCurrentStep() {
        if (currentStep == 0) {
            String name = fieldText(R.id.fieldName);
            if (name.isEmpty()) {
                toast(R.string.add_product_error_name);
                return false;
            }
            draft.name = name;
            draft.shortDesc = fieldText(R.id.fieldShortDesc);
            draft.fullDesc = fieldText(R.id.fieldFullDesc);
            draft.material = fieldText(R.id.fieldMaterial);
            draft.style = fieldText(R.id.fieldStyle);
            draft.brand = fieldText(R.id.fieldBrand);
            draft.pattern = fieldText(R.id.fieldPattern);
            draft.season = fieldText(R.id.fieldSeason);
            draft.body = fieldText(R.id.fieldBody);
            draft.gender = fieldText(R.id.fieldGender);
            draft.origin = fieldText(R.id.fieldOrigin);
            return true;
        }
        if (currentStep == 1) {
            String sku = fieldText(R.id.fieldSku);
            if (sku.isEmpty()) {
                toast(R.string.add_product_error_sku);
                return false;
            }
            String priceText = fieldText(R.id.fieldPrice);
            long price;
            try {
                price = priceText.isEmpty() ? 0 : Long.parseLong(priceText);
            } catch (NumberFormatException e) {
                toast(R.string.add_product_error_price);
                return false;
            }
            String stockText = fieldText(R.id.fieldStock);
            int stock = stockText.isEmpty() ? 0 : Integer.parseInt(stockText);
            draft.sku = sku;
            draft.price = price;
            draft.stock = stock;
            return true;
        }
        if (currentStep == 2) {
            // Image: dùng placeholder mặc định
            draft.imageRes = R.drawable.bg_product_2;
            return true;
        }
        if (currentStep == 3) {
            draft.category = fieldText(R.id.fieldCategory);
            draft.collection = fieldText(R.id.fieldCollection);
            return true;
        }
        if (currentStep == 4) {
            draft.status = pickedStatus;
            draft.visible = switchVisible.isChecked();
            return true;
        }
        return true;
    }

    private void saveProduct() {
        ProductRepository.getInstance().add(draft);
        Toast.makeText(this, R.string.add_product_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void toast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
