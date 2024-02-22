package com.codeflix.admin.catalogo.application.genre.create;

import com.codeflix.admin.catalogo.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput create(final String anId) {
        return new CreateGenreOutput(anId);
    }

    public static CreateGenreOutput create(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
