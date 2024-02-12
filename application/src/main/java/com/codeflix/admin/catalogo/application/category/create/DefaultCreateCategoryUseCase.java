package com.codeflix.admin.catalogo.application.category.create;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand input) {
        final var aName = input.name();
        final var aDescription = input.description();
        final var isActive = input.isActive();

        final var aCategory = Category.createCategory(aName, aDescription, isActive);
        final var notification = Notification.create();
        aCategory.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
        try {
            return API.Right(CreateCategoryOutput.create(this.categoryGateway.create(aCategory)));
        } catch (Throwable t) {
            return API.Left(Notification.create(t));
        }
    }
}
