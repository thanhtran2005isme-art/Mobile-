package com.example.banquanao.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Format tiền VND theo kiểu "100.016.500đ".
 */
public final class MoneyFormatter {

    private MoneyFormatter() { }

    private static final DecimalFormat FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        FORMAT = new DecimalFormat("#,###", symbols);
    }

    public static String vnd(long amount) {
        return FORMAT.format(amount) + "đ";
    }
}
