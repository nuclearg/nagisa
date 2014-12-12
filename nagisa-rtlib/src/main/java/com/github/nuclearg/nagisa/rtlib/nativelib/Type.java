package com.github.nuclearg.nagisa.rtlib.nativelib;

public class Type {
    public static String itoa(long i) {
        return String.valueOf(i);
    }

    public static long atoi(String str) {
        return Long.parseLong(str);
    }
}
