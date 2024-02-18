package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import javax.management.DescriptorKey;

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
    @DescriptorKey("Should return a new category when calls create")
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
    @DescriptorKey("Should return an updated category when calls update")
    void shouldReturnAnUpdatedCategoryWhenCallUpdate() {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;

        final Category aCategory = Category.createCategory("Films", "", expectedIsActive);

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

}
