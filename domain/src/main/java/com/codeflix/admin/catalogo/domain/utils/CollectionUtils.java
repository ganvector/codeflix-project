package com.codeflix.admin.catalogo.domain.utils;

import com.codeflix.admin.catalogo.domain.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {
    private CollectionUtils() {}

    public static <IN, OUT> Set<OUT> toSet(Collection<IN> values, Function<IN, OUT> mapper) {
        return values.stream().map(mapper).collect(Collectors.toSet());
    }
}
