package com.codeflix.admin.catalogo.application.category.update;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void shouldUpdateACategoryWhenAllInputsAreValid() {
        final var expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var aCategory = Category.createCategory("Films", null, true);
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito
                .when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));
        Mockito
                .when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(aCommand).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(anUpdatedCategory ->
                Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getCreatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())
        ));
    }
}
