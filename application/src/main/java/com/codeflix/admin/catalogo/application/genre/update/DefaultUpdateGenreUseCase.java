package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.load(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.active();
        final var categories = toCategoryID(aCommand.categories());

        final var aGenre = genreGateway.findById(anId)
                .orElseThrow(genreNotFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if(notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Genre %s".formatted(aCommand.id()), notification);
        }

        return UpdateGenreOutput.create(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryID> categoryIDS) {
        final var notification = Notification.create();

        if (categoryIDS == null || categoryIDS.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(categoryIDS);
        if (categoryIDS.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(categoryIDS);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private List<CategoryID> toCategoryID(List<String> categories) {
        return categories.stream().map(CategoryID::load).toList();
    }

    private Supplier<DomainException> genreNotFound(final GenreID anId) {
        return () -> NotFoundException.raise(Genre.class, anId);
    }
}
