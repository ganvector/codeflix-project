package com.codeflix.admin.catalogo.application.category.retrieve.get;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public GetCategoryByIdOutput execute(String anId) {
        final var categoryId = CategoryID.load(anId);
        final var aCategory = this.categoryGateway.findById(categoryId)
                .orElseThrow(categoryNotFound(categoryId));
        return GetCategoryByIdOutput.create(aCategory);
    }

    private Supplier<DomainException> categoryNotFound(final CategoryID anId) {
        return () -> NotFoundException.raise(Category.class, anId);
    }
}
