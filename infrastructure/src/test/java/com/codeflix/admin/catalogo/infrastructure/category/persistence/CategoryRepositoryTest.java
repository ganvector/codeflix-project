package com.codeflix.admin.catalogo.infrastructure.category.persistence;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldReturnAnErrorWhenGivenAnInvalidName() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.name";

        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);

        final var anCategoryEntity = CategoryJPAEntity.create(aCategory);
        anCategoryEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anCategoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void shouldReturnAnErrorWhenGivenAnInvalidCreatedAt() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.createdAt";

        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);

        final var anCategoryEntity = CategoryJPAEntity.create(aCategory);
        anCategoryEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anCategoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void shouldReturnAnErrorWhenGivenAnInvalidUpdatedAt() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.updatedAt";

        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);

        final var anCategoryEntity = CategoryJPAEntity.create(aCategory);
        anCategoryEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anCategoryEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
