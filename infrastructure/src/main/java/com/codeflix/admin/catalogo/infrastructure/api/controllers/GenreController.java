package com.codeflix.admin.catalogo.infrastructure.api.controllers;

import com.codeflix.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.infrastructure.api.GenreAPI;
import com.codeflix.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.presenter.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {
    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;

    public GenreController(final CreateGenreUseCase createGenreUseCase,  final GetGenreByIdUseCase getGenreByIdUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        CreateGenreCommand command = CreateGenreCommand.create(input.name(), input.active(), input.categories());
        CreateGenreOutput output = this.createGenreUseCase.execute(command);
        return ResponseEntity.created(URI.create("/genres/"+output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(String anId) {
        GenreOutput output = this.getGenreByIdUseCase.execute(anId);
        return GenreApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updateById(String anId, UpdateGenreRequest body) {
        return null;
    }

    @Override
    public void deleteById(String anId) {

    }
}
