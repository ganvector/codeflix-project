package com.codeflix.admin.catalogo.application.category.update;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() { categoryRepository.deleteAll(); }

    @Test
    public void shouldUpdateACategoryWhenAllInputsAreValid() {
        final var aCategory = Category.createCategory("Films", null, true);
        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        final var expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        Assertions.assertEquals(1, categoryRepository.count());

        final var aCommand = UpdateCategoryCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var output = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var actualCategory = categoryRepository.findById(output.id().getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void shouldReturnAnExceptionWhenInputNameIsNotValid() {
        final var aCategory = Category.createCategory("Films", null, true);
        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.create(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void shouldUpdateACategoryToInactive() {
        final var aCategory = Category.createCategory("Films", null, true);
        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        final var expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var output = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        final var actualCategory = categoryRepository.findById(output.id().getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(actualCategory.getCreatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void shouldReturnAnExceptionFromTheGateway() {
        final var aCategory = Category.createCategory("Films", null, true);
        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        final var expectedId = aCategory.getId();
        final String expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.create(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(1, categoryRepository.count());

        Mockito
                .doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(Mockito.any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.any());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), actualCategory.getCreatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void shouldReturnAnExceptionWhenIdNotFound() {
        final var expectedId = "123";
        final String expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.create(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.argThat(anId -> Objects.equals(CategoryID.load(expectedId), anId)));
        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());
    }
}
