package com.coffee.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class VNString {
    public static char removeAccent(char c) {
        String s = String.valueOf(c);
        s = removeAccent(s);
        return s.charAt(0);
    }

    public static String removeAccent(String str) {
        str = removeUpperCaseAccent(str);
        str = removeLowerCaseAccent(str);
        return str;
    }

    public static String removeUpperCaseAccent(String str) {
        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("[Đ]", "D");
        return str;
    }

    public static String removeLowerCaseAccent(String str) {
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("[đ]", "d");
        return str;
    }

    public static String currency(double money) {
        Locale locale = new Locale.Builder().setLanguage("vi").setRegion("VN").build();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(money);
    }
    public static double parseCurrency(String currencyString) {
        String cleanedString = currencyString.replaceAll("[^\\d]", "");

        double result = Double.parseDouble(cleanedString);

        return result;
    }
    public static boolean containsUnicode(String str) {
        return str.chars().anyMatch(c -> c >= 128);
    }

    public static boolean containsUpperCase(String str) {
        return str.chars().anyMatch(Character::isUpperCase);
    }

    public static boolean containsLowerCase(String str) {
        return str.chars().anyMatch(Character::isLowerCase);
    }

    public static boolean containsAlphabet(String str) {
        return str.chars().anyMatch(Character::isAlphabetic);
    }

    public static boolean containsNumber(String str) {
        return str.chars().anyMatch(Character::isDigit);
    }

    public static boolean containsAlphaNumber(String str) {
        return str.matches(".*[a-zA-Z].*") && str.matches(".*\\d.*");
    }

    public static boolean containsSpecial(String str) {
        return str.chars().anyMatch(c -> !(Character.isLetterOrDigit(c) || Character.isWhitespace(c)));
    }

    public static boolean checkFormatPhone(String str) {
        return str.matches("^(\\+?84|0)[35789]\\d{8}$");
    }

    public static boolean checkNo(String str) {
        return str.matches("^0\\d{11}$");
    }

    public static boolean checkFormatOfEmail(String str) {
        return str.matches("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");
    }

    public static boolean checkFormatDate(String str) {
        if (str.matches("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$"))
            return true;
        return str.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}$");
    }

    public static boolean checkFormatDateTime(String str) {
        if (str.matches("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]) (\\d{2}:\\d{2}(?::\\d{2}(\\.\\d{1,6})?)?)?$"))
            return true;
        return str.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4} (\\d{2}:\\d{2}(?::\\d{2}(\\.\\d{1,6})?)?)?$");
    }

    public static boolean checkRangeOfPercent(String str) {
        try {
            return Double.parseDouble(str) > 0 && Double.parseDouble(str) < 100;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean checkUnsignedNumber(String str) {
        try {
            return Double.parseDouble(str) >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String inputString = "078203023644";
        System.out.println(VNString.containsAlphabet(inputString));
    }
}
