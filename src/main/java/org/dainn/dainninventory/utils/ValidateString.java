package org.dainn.dainninventory.utils;

public class ValidateString {
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }
    public static String trimString(String str) {
        return str == null ? null : str.trim();
    }
}
