package com.codeflix.admin.catalogo.domain.genre;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.utils.InstantUtils;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

    private GenreID id;
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    public static Genre createGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.generateUnique();
        final var now = InstantUtils.now();
        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, isActive ? null : now);
    }

    public static Genre load(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, anUpdatedAt, aDeletedAt);
    }

    public Genre deactivate() {
        if (deletedAt == null) {
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre update(
            final String aName,
            final boolean isActive,
            final List<CategoryID> aCategories
    ) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.categories = new ArrayList<>(aCategories != null ? aCategories : new ArrayList<>());
        this.updatedAt = InstantUtils.now();

        selfValidate();

        return this;
    }

    public Genre addCategory(final CategoryID categoryID) {
        if (categoryID == null) {
            return this;
        }
        this.categories.add(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> categoryIDS) {
        if (categoryIDS == null || categoryIDS.isEmpty()) {
            return this;
        }
        this.categories.addAll(categoryIDS);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID categoryID) {
        if (categoryID == null) {
            return this;
        }
        this.categories.remove(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public Genre clone() {
        final var anId = GenreID.load(getId().getValue());
        return new Genre(
                anId,
                this.getName(),
                this.isActive(),
                new ArrayList<>(this.categories),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return categories;
    }

    private void setCategories(List<CategoryID> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if(notification.hasError()) {
            throw new NotificationException("", notification);
        }
    }
}
