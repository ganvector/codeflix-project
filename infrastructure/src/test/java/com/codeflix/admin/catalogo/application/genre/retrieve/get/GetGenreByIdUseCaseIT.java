package com.codeflix.admin.catalogo.application.genre.retrieve.get;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@IntegrationTest
public class GetGenreByIdUseCaseIT {
    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @Test
    public void shouldReturnGenreWhenGivenAValidId() {
        final Category movies = Category.createCategory("Movies", null, true);
        final Category series = Category.createCategory("Series", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                movies.getId(), series.getId()
        );

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();
        genreGateway.create(aGenre);

        final var actualGenre = getGenreByIdUseCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(sortString(asString(expectedCategories)), sortString(actualGenre.categories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(expectedId);
    }

    @Test
    public void shouldReturnNotFoundGenreWhenGivenAValidIdThatDoesNotExists() {
        final var expectedErrorMessage = "Genre with ID not_exists was not found";

        final var expectedId = GenreID.load("not_exists");

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> getGenreByIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(expectedId);
    }

    private List<String> asString (final List<CategoryID> genreIDS) {
        return genreIDS.stream().map(CategoryID::getValue).toList();
    }

    private static List<CategoryID> sortCategoriesIds(List<CategoryID> expectedCategories) {
        return expectedCategories.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }

    private static List<String> sortString(final List<String> stringList) {
        return stringList.stream().sorted().toList();
    }
}
