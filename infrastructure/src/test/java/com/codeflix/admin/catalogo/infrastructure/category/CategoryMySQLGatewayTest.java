package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.MySQLGatewayTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return a new category when calls create")
    void shouldReturnANewCategoryWhenCallsCreate() {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;

        final Category aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var categoryEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), categoryEntity.getId());
        Assertions.assertEquals(expectedName, categoryEntity.getName());
        Assertions.assertEquals(expectedDescription, categoryEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, categoryEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), categoryEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), categoryEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), categoryEntity.getDeletedAt());
        Assertions.assertNull(categoryEntity.getDeletedAt());
    }

    @Test
    @DisplayName("Should return an updated category when calls update")
    void shouldReturnAnUpdatedCategoryWhenCallUpdate() {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;

        final Category aCategory = Category.createCategory("Films", "", expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var invalidCategory = categoryRepository.findById(aCategory.getId().getValue()).get().toAggregate();
        Assertions.assertEquals("Films", invalidCategory.getName());
        Assertions.assertEquals("", invalidCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, invalidCategory.isActive());

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryGateway.update(anUpdatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var categoryEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), categoryEntity.getId());
        Assertions.assertEquals(expectedName, categoryEntity.getName());
        Assertions.assertEquals(expectedDescription, categoryEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, categoryEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), categoryEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(categoryEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), categoryEntity.getDeletedAt());
        Assertions.assertNull(categoryEntity.getDeletedAt());
    }

    @Test
    @DisplayName("Should delete a pre persisted category")
    void shouldDeleteAPrePersistedCategory() {
        final var aCategory = Category.createCategory("Movies", "", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    @DisplayName("Should not delete a pre persisted category when id is invalid")
    void shouldNotDeleteAPrePersistedCategoryWhenIdIsInvalid() {
        final var aCategory = Category.createCategory("Movies", "", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.load("invalid"));

        Assertions.assertEquals(1, categoryRepository.count());
    }

    @Test
    @DisplayName("Should return a category when calls findByID with valid ID")
    void shouldReturnACategoryWhenCallsFindByIDWithValidID () {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;

        final Category aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    @DisplayName("Should not return a category when calls findByID with invalid ID")
    void shouldNotReturnACategoryWhenCallsFindByIDWithInvalidID () {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;

        final Category aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(CategoryID.load("invalid"));

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    @DisplayName("Should return paginated when calls findAll")
    void shouldReturnPaginatedWhenCallsFindAll() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var moviesCategory = Category.createCategory("Movies", null, true);
        final var seriesCategory = Category.createCategory("Series", null, true);
        final var animesCategory = Category.createCategory("Animes", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJPAEntity.create(moviesCategory),
                CategoryJPAEntity.create(seriesCategory),
                CategoryJPAEntity.create(animesCategory)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "desc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(seriesCategory.getId(), actualResult.items().get(0).getId());
    }

    @Test
    @DisplayName("Should return empty page when calls findAll with empty database")
    void shouldReturnEmptyPageWhenCallsFindAllWithEmptyDatabase () {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "desc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPage, actualResult.items().size());
    }

    @Test
    @DisplayName("Should return paginated when calls findAll with page 1")
    void shouldReturnPaginatedWhenCallsFindAllWithPage1 () {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var moviesCategory = Category.createCategory("Movies", null, true);
        final var seriesCategory = Category.createCategory("Series", null, true);
        final var animesCategory = Category.createCategory("Animes", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJPAEntity.create(moviesCategory),
                CategoryJPAEntity.create(seriesCategory),
                CategoryJPAEntity.create(animesCategory)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        // Page 0
        var query = new SearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(animesCategory.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;

        query = new SearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(moviesCategory.getId(), actualResult.items().get(0).getId());

        // page 2
        expectedPage = 2;

        query = new SearchQuery(2, 1, "", "name", "asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(seriesCategory.getId(), actualResult.items().get(0).getId());
    }

    @Test
    @DisplayName("Should return paginated when calls findAll and \"ries\" as a term")
    void shouldReturnPaginatedWhenCallsFindAllAndMovAsTerm() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var moviesCategory = Category.createCategory("Movies", null, true);
        final var seriesCategory = Category.createCategory("Series", null, true);
        final var animesCategory = Category.createCategory("Animes", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJPAEntity.create(moviesCategory),
                CategoryJPAEntity.create(seriesCategory),
                CategoryJPAEntity.create(animesCategory)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "ries", "name", "desc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(seriesCategory.getId(), actualResult.items().get(0).getId());
    }

    @Test
    @DisplayName("Should return paginated when calls findAll and \"MATION\" as a term")
    void shouldReturnPaginatedWhenCallsFindAllAndUniverseAsTerm() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var moviesCategory = Category.createCategory("Movies", "Direct from the big screen", true);
        final var seriesCategory = Category.createCategory("Series", "Series to all family", true);
        final var animesCategory = Category.createCategory("Animes", "The best of animation", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJPAEntity.create(moviesCategory),
                CategoryJPAEntity.create(seriesCategory),
                CategoryJPAEntity.create(animesCategory)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "MATION", "name", "desc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(animesCategory.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void shouldReturnIdsWhenCallExistsByIds() {
        final var moviesCategory = Category.createCategory("Movies", "Direct from the big screen", true);
        final var seriesCategory = Category.createCategory("Series", "Series to all family", true);
        final var animesCategory = Category.createCategory("Animes", "The best of animation", true);

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJPAEntity.create(moviesCategory),
                CategoryJPAEntity.create(seriesCategory),
                CategoryJPAEntity.create(animesCategory)
        ));

        final var expectedIds = List.of(moviesCategory.getId(), animesCategory.getId());
        final var ids = List.of(moviesCategory.getId(), animesCategory.getId(), CategoryID.generateUnique());

        final var actualResult = categoryGateway.existsByIds(ids);

        Assertions.assertTrue(actualResult.containsAll(expectedIds));
    }
}
