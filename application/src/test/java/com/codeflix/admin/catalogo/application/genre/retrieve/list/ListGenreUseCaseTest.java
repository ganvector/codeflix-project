package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase listGenreUseCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void shouldReturnGenresGivenAValidQuery () {
        final var genres = List.of(
                Genre.createGenre("Horror", true),
                Genre.createGenre("Adventure", true),
                Genre.createGenre("Romance", true)
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "R";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var expectedItems = genres.stream()
                .map(ListGenreOutput::create)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        final var aQuery = new SearchQuery(expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito
                .when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

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
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "R";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var expectedItems = List.<ListGenreOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        final var aQuery = new SearchQuery(expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito
                .when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

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
    public void shouldReturnAnExceptionWhenAnUnexpectedExceptionHappen () {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "R";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage= "Gateway error";


        final var aQuery = new SearchQuery(expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito
                .when(genreGateway.findAll(Mockito.any()))
                .thenThrow(new IllegalStateException("Gateway error"));


        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> listGenreUseCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findAll(Mockito.eq(aQuery));
    }
}
