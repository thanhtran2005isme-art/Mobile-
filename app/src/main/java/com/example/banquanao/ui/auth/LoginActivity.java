package com.example.banquanao.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.banquanao.MainActivity;
import com.example.banquanao.R;
import com.example.banquanao.data.auth.SessionManager;
import com.example.banquanao.data.auth.User;

public class LoginActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        session = new SessionManager(this);

        // Auto skip nếu đã đăng nhập
        if (session.isLoggedIn()) {
            goToMain();
            return;
        }

        EditText inputPhone = findViewById(R.id.inputPhone);
        EditText inputPassword = findViewById(R.id.inputPassword);
        TextView btnSubmit = findViewById(R.id.btnSubmit);
        TextView linkRegister = findViewById(R.id.linkRegister);

        // Render link "Chưa có tài khoản? Đăng ký ngay"
        String full = getString(R.string.login_no_account) + " " + getString(R.string.login_register_link);
        SpannableString ss = new SpannableString(full);
        int start = full.indexOf(getString(R.string.login_register_link));
        int end = start + getString(R.string.login_register_link).length();
        ss.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(this, R.color.login_link)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkRegister.setText(ss);

        linkRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        btnSubmit.setOnClickListener(v -> doLogin(
                inputPhone.getText().toString().trim(),
                inputPassword.getText().toString()));

        // Nút "Đăng nhập bằng SĐT" focus vào input
        findViewById(R.id.btnPhoneLogin).setOnClickListener(v -> inputPhone.requestFocus());
    }

    private void doLogin(String phone, String password) {
        if (phone.isEmpty()) {
            toast(R.string.error_phone_required);
            return;
        }
        if (password.isEmpty()) {
            toast(R.string.error_password_required);
            return;
        }
        User user = session.login(phone, password);
        if (user == null) {
            toast(R.string.error_invalid_credentials);
            return;
        }
        goToMain();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
