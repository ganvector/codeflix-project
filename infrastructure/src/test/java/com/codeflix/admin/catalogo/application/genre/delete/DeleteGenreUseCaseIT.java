package com.codeflix.admin.catalogo.application.genre.delete;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private DeleteGenreUseCase deleteGenreUseCase;

    @Test
    public void shouldDeleteAGenreWhenGivenAValidGenreId () {
        final var aGenre = Genre.createGenre("Horror", true);
        final var expectedId = aGenre.getId();

        genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, genreRepository.count());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .deleteById(expectedId);
    }

    @Test
    public void shouldDoNothingWhenGivenAnInvalidGenreId () {
        final var aGenre = Genre.createGenre("Horror", true);
        genreGateway.create(aGenre);
        final var expectedId = GenreID.load("Horror");

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, genreRepository.count());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .deleteById(expectedId);
    }
}
