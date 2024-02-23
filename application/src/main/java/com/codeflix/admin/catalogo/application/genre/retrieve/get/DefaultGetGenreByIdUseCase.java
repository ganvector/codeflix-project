package com.codeflix.admin.catalogo.application.genre.retrieve.get;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anId) {
        final GenreID aGenreId = GenreID.load(anId);
        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::create)
                .orElseThrow(() -> NotFoundException.raise(Genre.class, aGenreId));
    }
}
