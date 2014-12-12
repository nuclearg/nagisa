package com.github.nuclearg.nagisa.rtlib.nativelib;

public class Type {
    public static String chr(long i) {
        return "" + (char) i;
    }

    public static long asc(String ch) {
        return (long) ch.charAt(0);
    }
}
