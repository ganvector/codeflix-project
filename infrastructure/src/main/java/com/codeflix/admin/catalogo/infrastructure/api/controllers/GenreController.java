package com.codeflix.admin.catalogo.infrastructure.api.controllers;

import com.codeflix.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.ListGenreOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
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
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenreUseCase listGenreUseCase
    ) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        CreateGenreCommand command = CreateGenreCommand.create(input.name(), input.active(), input.categories());
        CreateGenreOutput output = this.createGenreUseCase.execute(command);
        return ResponseEntity.created(URI.create("/genres/"+output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String direction) {
        final SearchQuery query = new SearchQuery(page, perPage, search, sort, direction);
        final Pagination<ListGenreOutput> output = this.listGenreUseCase.execute(query);
        return output.map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(String anId) {
        GenreOutput output = this.getGenreByIdUseCase.execute(anId);
        return GenreApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updateById(String anId, UpdateGenreRequest body) {
        final UpdateGenreCommand command = UpdateGenreCommand.create(
                anId,
                body.name(),
                body.active(),
                body.categories()
        );
        UpdateGenreOutput output = this.updateGenreUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<Void> deleteById(String anId) {
        this.deleteGenreUseCase.execute(anId);
        return ResponseEntity.noContent().build();
    }
}
