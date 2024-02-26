package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class ListGenreUseCaseIT {
    @Autowired
    private ListGenreUseCase listGenreUseCase;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void shouldReturnGenresGivenAValidQuery () {
        final var genres = List.of(
                Genre.createGenre("Horror", true),
                Genre.createGenre("Adventure", true),
                Genre.createGenre("Romance", true)
        );
        genreRepository.saveAllAndFlush(genres.stream().map(GenreJpaEntity::load).toList());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "R";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var expectedItems = genres.stream()
                .map(ListGenreOutput::create)
                .toList();

        final var aQuery = new SearchQuery(expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualOutput = listGenreUseCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findAll(Mockito.eq(aQuery));
    }

    @Test
    public void shouldReturnEmptyGenresGivenAValidQuery () {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "R";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<ListGenreOutput>of();

        final var aQuery = new SearchQuery(expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualOutput = listGenreUseCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findAll(Mockito.eq(aQuery));
    }
}
