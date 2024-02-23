package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record ListGenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {
    public static ListGenreOutput create(final Genre aGenre) {
        return new ListGenreOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
