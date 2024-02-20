package com.codeflix.admin.catalogo.application.category.retrieve.list;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

public record ListCategoriesOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static ListCategoriesOutput create(final Category aCategory) {
        return new ListCategoriesOutput(
            aCategory.getId(),
            aCategory.getName(),
            aCategory.getDescription(),
            aCategory.isActive(),
            aCategory.getCreatedAt(),
            aCategory.getUpdatedAt(),
            aCategory.getDeletedAt()
        );
    }
}
