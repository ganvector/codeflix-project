package com.codeflix.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like (final String property, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(property)), "%"+term.toUpperCase()+"%");
    }
}
