package com.codeflix.admin.catalogo.application.category.update;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand input) {
        final CategoryID anId = CategoryID.load(input.id());
        final String aName = input.name();
        final String aDescription = input.description();
        final boolean isActive = input.isActive();

        final var aCategory = this.categoryGateway.findById(anId).orElseThrow(categoryNotFound(anId));

        final var notification = Notification.create();
        aCategory.update(aName, aDescription, isActive).validate(notification);

        return notification.hasError() ? API.Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        try {
            return API.Right(UpdateCategoryOutput.create(this.categoryGateway.update(aCategory)));
        } catch (Throwable t) {
            return API.Left(Notification.create(t));
        }
    }

    private Supplier<DomainException> categoryNotFound(final CategoryID anId) {
        return () -> NotFoundException.raise(Category.class, anId);
    }

}
