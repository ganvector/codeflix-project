package com.codeflix.admin.catalogo.application.video.delete;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase deleteVideoUseCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallingDeleteVideo_shouldCallsDeleteVideo() {
        // given
        final var expectedVideoId = VideoID.generateUnique();

        doNothing()
            .when(videoGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> this.deleteVideoUseCase.execute(expectedVideoId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedVideoId);
    }

    @Test
    public void givenAValidId_whenCallingDeleteVideoAndGatewayThrows_shouldReceiveException() {
        // given
        final var expectedVideoId = VideoID.generateUnique();
        final var expectedErrorMessage = "An error on delete video was observed [videoId:%s]"
                .formatted(expectedVideoId.getValue());

        doThrow(new RuntimeException("Error on delete"))
                .when(videoGateway).deleteById(any());

        // when
        final var output = Assertions.assertThrows(
                InternalErrorException.class,
                () -> this.deleteVideoUseCase.execute(expectedVideoId.getValue())
        );

        // then
        verify(videoGateway).deleteById(expectedVideoId);
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());
    }
}
