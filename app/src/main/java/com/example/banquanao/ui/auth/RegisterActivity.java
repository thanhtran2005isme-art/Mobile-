package com.example.banquanao.ui.auth;

import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.R;
import com.example.banquanao.data.auth.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private SessionManager session;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        session = new SessionManager(this);

        EditText inputName = findViewById(R.id.inputName);
        EditText inputPhone = findViewById(R.id.inputPhone);
        EditText inputPassword = findViewById(R.id.inputPassword);
        ImageView btnTogglePassword = findViewById(R.id.btnTogglePassword);
        TextView btnSubmit = findViewById(R.id.btnSubmit);
        TextView linkLogin = findViewById(R.id.linkLogin);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnTogglePassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            inputPassword.setInputType(passwordVisible
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputPassword.setSelection(inputPassword.getText().length());
            btnTogglePassword.setImageResource(passwordVisible
                    ? R.drawable.ic_eye_on
                    : R.drawable.ic_eye_off);
        });

        // "Đã có tài khoản? Đăng nhập" — phần "Đăng nhập" tô tím
        String full = getString(R.string.register_have_account) + " " + getString(R.string.register_login_link);
        SpannableString ss = new SpannableString(full);
        int start = full.indexOf(getString(R.string.register_login_link));
        int end = start + getString(R.string.register_login_link).length();
        ss.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(this, R.color.login_link)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkLogin.setText(ss);
        linkLogin.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> doRegister(
                inputName.getText().toString().trim(),
                inputPhone.getText().toString().trim(),
                inputPassword.getText().toString()));
    }

    private void doRegister(String name, String phone, String password) {
        if (name.isEmpty()) {
            toast(R.string.error_name_required);
            return;
        }
        if (phone.isEmpty()) {
            toast(R.string.error_phone_required);
            return;
        }
        boolean ok = session.register(name, phone, password);
        if (!ok) {
            Toast.makeText(this, "Số điện thoại đã được sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }
        toast(R.string.register_success);
        finish();
    }

    private void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
