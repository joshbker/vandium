package dev.devous.vandium.format;

import java.text.DecimalFormat;
import java.util.Arrays;

public class NumberFormat {

    private static final DecimalFormat format = new DecimalFormat("#,###");

    private NumberFormat() {

    }

    public static String format(int i) {
        String formatted = format.format(i);
        if (formatted.length() > 6) {
            String[] base = formatted.split(",");
            String format = base[0] + "." + base[1].charAt(0);

            if (formatted.length() < 9) {
                format += "k";
            } else {
                format += "mil";
            }

            return format;
        }

        return formatted;
    }

}
