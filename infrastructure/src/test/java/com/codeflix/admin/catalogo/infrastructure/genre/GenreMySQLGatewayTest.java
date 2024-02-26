package com.codeflix.admin.catalogo.infrastructure.genre;

import com.codeflix.admin.catalogo.MySQLGatewayTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.category.CategoryMySQLGateway;

import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void shouldPersistGenreWhenCalledCreateGenre() {
        final var movies = Category.createCategory("Movies", null, true);
        categoryGateway.create(movies);

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final Genre aGenre = Genre.createGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();
        aGenre.addCategories(expectedCategories);

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }



    @Test
    public void shouldPersistGenreWithNoCategoryWhenCalledCreateGenre() {
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final Genre aGenre = Genre.createGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateGenreAddingNewCategoryWhenCallingUpdate() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(movies.getId(), series.getId());

        final Genre aGenre = Genre.createGenre("Terror", expectedIsActive);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("Terror", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());


        final var actualGenre = genreGateway.update(
                aGenre.clone().update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories.size(), persistedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateGenreRemovingCategoryWhenCallingUpdate() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final Genre aGenre = Genre.createGenre("Terror", expectedIsActive);
        final var expectedId = aGenre.getId();
        aGenre.addCategories(List.of(movies.getId(), series.getId()));

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("Terror", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());


        final var actualGenre = genreGateway.update(
                aGenre.clone().update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAGenreActivatingItWhenCallingUpdate() {
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final Genre aGenre = Genre.createGenre("Terror", false);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("Terror", aGenre.getName());
        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());


        final var actualGenre = genreGateway.update(
                aGenre.clone().update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAGenreInactivatingItWhenCallingUpdate() {
        final var expectedName = "Horror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final Genre aGenre = Genre.createGenre("Terror", true);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("Terror", aGenre.getName());
        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());


        final var actualGenre = genreGateway.update(
                aGenre.clone().update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());


        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(persistedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldDeleteAPersistedGenreByItsIdWhenValid() {
        final Genre aGenre = Genre.createGenre("Horror", true);

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        genreGateway.deleteById(aGenre.getId());

        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void shouldDeleteAPersistedGenreByItsIdWhenInvalid() {
        final Genre aGenre = Genre.createGenre("Horror", true);

        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        genreGateway.deleteById(GenreID.load("123"));

        Assertions.assertEquals(1, genreRepository.count());
    }

    @Test
    public void shouldReturnGenreWhenCallFindById() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(movies.getId(), series.getId());

        Assertions.assertEquals(0, genreRepository.count());

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        genreRepository.saveAndFlush(GenreJpaEntity.load(aGenre));
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());

        final var actualGenre = genreGateway.findById(aGenre.getId()).get();

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldReturnEmptyWhenCallFindByIdWithAnInvalidId() {
        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.findById(GenreID.generateUnique());

        Assertions.assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void shouldReturnEmptyGenresWhenThereIsNonePersisted() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");

        final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ac,0,10,1,Action",
            "erro,0,10,1,Terror",
            "com,0,10,1,Comedy",
            "fic,0,10,1,Science Fiction",
            "dr,0,10,1,Drama"
    })
    public void shouldReturnListOfGenresWhenCalledWithValidTerm(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedTotal,
            final String expectedGenreName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,Action",
            "name,desc,0,10,5,Terror",
            "createdAt,desc,0,10,5,Comedy",
            "createdAt,asc,0,10,5,Drama"
    })
    public void shouldReturnListOfGenresWhenCalledWithValidSortAndDirection(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedTotal,
            final String expectedGenreName
    ) {
        final var expectedTerms = "";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,5,2,Action;Comedy",
            "1,2,5,2,Drama;Science Fiction",
            "2,2,5,1,Terror",
    })
    public void shouldReturnListOfGenresWhenCalledWithValidPagination(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedTotal,
            final long expectedItemsCount,
            final String expectedGenreNames
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        mockGenres();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName: expectedGenreNames.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(
                List.of(
                        GenreJpaEntity.load(Genre.createGenre("Drama", true)),
                        GenreJpaEntity.load(Genre.createGenre("Terror", true)),
                        GenreJpaEntity.load(Genre.createGenre("Science Fiction", true)),
                        GenreJpaEntity.load(Genre.createGenre("Action", true)),
                        GenreJpaEntity.load(Genre.createGenre("Comedy", true))
                )
        );
    }

    private static List<CategoryID> sortCategoriesIds(List<CategoryID> expectedCategories) {
        return expectedCategories.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }
}
