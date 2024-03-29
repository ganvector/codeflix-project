package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput create(final String anId) {
        return new UpdateGenreOutput(anId);
    }

    public static UpdateGenreOutput create(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}