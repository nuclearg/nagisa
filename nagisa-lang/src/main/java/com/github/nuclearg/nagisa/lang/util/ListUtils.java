package com.github.nuclearg.nagisa.lang.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <S, T> List<T> transform(Iterable<S> list, Action<S, T> action) {
        List<T> result = new ArrayList<>();

        for (S value : list)
            result.add(action.convert(value));

        return result;
    }

    public static interface Action<S, T> {
        T convert(S value);
    }
}
