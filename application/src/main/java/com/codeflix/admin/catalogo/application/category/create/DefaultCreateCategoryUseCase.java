package com.codeflix.admin.catalogo.application.category.create;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand input) {
        final var aName = input.name();
        final var aDescription = input.description();
        final var isActive = input.isActive();

        final var aCategory = Category.createCategory(aName, aDescription, isActive);
        aCategory.validate(new ThrowsValidationHandler());

        this.categoryGateway.create(aCategory);
        return CreateCategoryOutput.create(aCategory);
    }
}
