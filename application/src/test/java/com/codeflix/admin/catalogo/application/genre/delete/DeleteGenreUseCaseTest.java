package com.codeflix.admin.catalogo.application.genre.delete;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultDeleteGenreUseCase deleteGenreUseCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void shouldDeleteAGenreWhenGivenAValidGenreId () {
        final var aGenre = Genre.createGenre("Horror", true);
        final var expectedId = aGenre.getId();

        Mockito
                .doNothing()
                .when(genreGateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .deleteById(expectedId);
    }

    @Test
    public void shouldDoNothingWhenGivenAnInvalidGenreId () {
        final var expectedId = GenreID.load("Horror");

        Mockito
                .doNothing()
                .when(genreGateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .deleteById(expectedId);
    }

    @Test
    public void shouldReceiveExceptionWhenGatewayThrowsUnexpectedError () {
        final var aGenre = Genre.createGenre("Horror", true);
        final var expectedId = aGenre.getId();

        Mockito
                .doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(Mockito.any());

        Assertions.assertThrows(IllegalStateException.class, () -> deleteGenreUseCase.execute(expectedId.getValue()));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .deleteById(expectedId);
    }
}
