package com.codeflix.admin.catalogo.domain.genre;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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

        final var notification = Notification.create();
        validate(notification);

        if(notification.hasError()) {
            throw new NotificationException("", notification);
        }
    }

    public static Genre createGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.generateUnique();
        final var now = Instant.now();
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

    @Override
    public Genre clone() {
        try {
            var aGenre = (Genre) super.clone();
            aGenre.setCategories(List.copyOf(this.getCategories()));
            return aGenre;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(this.categories);
    }

    private void setCategories(List<CategoryID> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
