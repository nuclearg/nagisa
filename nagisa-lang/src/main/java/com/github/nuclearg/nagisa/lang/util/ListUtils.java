package com.github.nuclearg.nagisa.lang.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <T> List<T> union(List<T> l1, List<T> l2) {
        List<T> list = new ArrayList<>();

        list.addAll(l1);
        list.addAll(l2);

        return list;
    }
}
