
package com.myst.kingdomheartsclock.utils;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

    public static Pattern pattern;
    public static Matcher matcher;

    static final String emailPattern = "\\A[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\z";
    static final String urlPattern = "\\A(https?://)(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})(:\\d+)?([\\/a-zA-Z0-9.\\-?=&#%+,;@!$~_[\\]]*)\\z";
    static final String userNamePattern = "((?=.*[a-zA-z0-9]).{3,})";
    static final String passwordPattern = "((?=.*[a-zA-z0-9]).{6,})";
    static final String numberPattern = "\\d+";

    public static boolean validateUserName(@NonNull String name) {
        pattern = Pattern.compile(userNamePattern);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean validatePassword(@NonNull String password) {
        pattern = Pattern.compile(passwordPattern);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validateNumber(@NonNull String number) {
        pattern = Pattern.compile(numberPattern);
        matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean validateWebsite(@NonNull String url) {
        return android.util.Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean validateEmail(@NonNull String email) {
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean validateDate(int day, int month, int year) {
        if (day > 0) {
            if (month == 2) {
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                    if (day <= 29) {
                        return true;
                    }
                } else {
                    if (day <= 28) {
                        return true;
                    }
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day <= 30) {
                    return true;
                }
            } else {
                if (day <= 31) {
                    return true;
                }
            }
        }
        return false;
    }
}
