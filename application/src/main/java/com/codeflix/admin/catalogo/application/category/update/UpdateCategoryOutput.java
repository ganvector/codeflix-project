package com.codeflix.admin.catalogo.application.category.update;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
        String id
) {
    public static UpdateCategoryOutput create(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput create(final String anId) {
        return new UpdateCategoryOutput(anId);
    }
}
