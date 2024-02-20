package com.codeflix.admin.catalogo.infrastructure.category.presenters;

import com.codeflix.admin.catalogo.application.category.retrieve.get.GetCategoryByIdOutput;
import com.codeflix.admin.catalogo.application.category.retrieve.list.ListCategoriesOutput;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final GetCategoryByIdOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final ListCategoriesOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
