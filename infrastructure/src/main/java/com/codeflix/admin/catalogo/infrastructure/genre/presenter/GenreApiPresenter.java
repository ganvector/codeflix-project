package com.codeflix.admin.catalogo.infrastructure.genre.presenter;

import com.codeflix.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.ListGenreOutput;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreResponse;

public class GenreApiPresenter {
    public static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.active(),
                output.categories(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static GenreListResponse present(final ListGenreOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
