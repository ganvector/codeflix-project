package com.codeflix.admin.catalogo.application.category.retrieve.list;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {
    @Autowired
    private ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @AfterEach
    void cleanUp() { categoryRepository.deleteAll(); }

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.createCategory("Movies", null, true),
                Category.createCategory("Series", null, true),
                Category.createCategory("Animes", null, true),
                Category.createCategory("Documentaries", null, true),
                Category.createCategory("Cartoons", "Made for kids", true),
                Category.createCategory("Sports", null, true),
                Category.createCategory("Codeflix Originals", null, true)
                ).map(CategoryJPAEntity::create).toList();
        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void shouldReturnEmptyCategoriesWhenHasNoResultsForTheQuery() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "K-drama";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;


        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, 0, List.of());

        final var expectedResult = expectedPagination.items();

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedResult, output.items());
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
    }

    @ParameterizedTest
    @CsvSource({
            "ovies,0,10,1,1,Movies",
            "erie,0,10,1,1,Series",
            "MES,0,10,1,1,Animes",
            "DOC,0,10,1,1,Documentaries",
            "kids,0,10,1,1,Cartoons",
            "flix,0,10,1,1,Codeflix Originals",
            "rts,0,10,1,1,Sports"
    })
    public void shouldReturnCategoriesFilteredWhenGivenAValidTerm (
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";


        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedCategoryName, output.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Animes",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Movies",
            "createdAt,desc,0,10,7,7,Codeflix Originals",
    })
    public void shouldReturnCategoriesSortedWhenGivenAValidSortAndDirection(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, "", expectedSort, expectedDirection
        );

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedCategoryName, output.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Animes;Cartoons",
            "1,2,2,7,Codeflix Originals;Documentaries",
            "2,2,2,7,Movies;Series",
            "3,2,1,7,Sports"
    })
    public void shouldReturnCategoriesPaginatedGivenAValidPage(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryNames
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, "", expectedSort, expectedDirection
        );

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        var index = 0;
        for (String expectedName : expectedCategoryNames.split(";")) {
            final String actualName = output.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
