package com.codeflix.admin.catalogo.application.video.retrieve.get;

import com.codeflix.admin.catalogo.application.Fixture;
import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.utils.CollectionUtils;
import com.codeflix.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase getVideoByIdUseCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidVideoId_whenCallingGetVideoById_thenReturnVideo() {
        // given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Videos.category().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Videos.genre().getId());
        final var expectedCastMembers = Set.<CastMemberID>of(Fixture.Videos.castMember().getId());

        final Video video = Video.create(
                expectedTitle,
                expectedDescription,
                launchYear,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final VideoID expectedId = video.getId();

        final AudioVideoMedia expectedVideo = generateMockVideo(Resource.Type.VIDEO);
        final AudioVideoMedia expectedTrailer = generateMockVideo(Resource.Type.TRAILER);
        final ImageMedia expectedBanner = generateMockImage(Resource.Type.BANNER);
        final ImageMedia expectedThumbnail = generateMockImage(Resource.Type.THUMBNAIL);
        final ImageMedia expectedThumbnailHalf = generateMockImage(Resource.Type.THUMBNAIL_HALF);

        video.setVideo(expectedVideo);
        video.setTrailer(expectedTrailer);
        video.setBanner(expectedBanner);
        video.setThumbnail(expectedThumbnail);
        video.setThumbnailHalf(expectedThumbnailHalf);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.from(video)));

        // when
        final var output = getVideoByIdUseCase.execute(video.getId().getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedTitle, output.title());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(launchYear.getValue(), output.launchedAt());
        Assertions.assertEquals(expectedDuration, output.duration());
        Assertions.assertEquals(expectedOpened, output.opened());
        Assertions.assertEquals(expectedPublished, output.published());
        Assertions.assertEquals(expectedRating.getName(), output.rating());
        Assertions.assertTrue(expectedCategories.size() == output.categories().size());
        Assertions.assertTrue(asString(expectedCategories).containsAll(output.categories()));
        Assertions.assertTrue(expectedGenres.size() == output.genres().size());
        Assertions.assertTrue(asString(expectedGenres).containsAll(output.genres()));
        Assertions.assertTrue(expectedCastMembers.size() == output.castMembers().size());
        Assertions.assertTrue(asString(expectedCastMembers).containsAll(output.castMembers()));
        Assertions.assertEquals(expectedVideo, output.video());
        Assertions.assertEquals(expectedTrailer, output.trailer());
        Assertions.assertEquals(expectedBanner, output.banner());
        Assertions.assertEquals(expectedThumbnail, output.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, output.thumbnailHalf());
        Assertions.assertEquals(video.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(video.getUpdatedAt(), output.updatedAt());
    }

    @Test
    public void givenAInvalidVideoId_whenCallingGetVideoById_shouldReturnNotFound() {
        // given
        final var expectedId = VideoID.generateUnique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var output = Assertions.assertThrows(NotFoundException.class, () -> getVideoByIdUseCase.execute(expectedId.getValue()));

        // then
        verify(videoGateway).findById(expectedId);
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());
    }

    private AudioVideoMedia generateMockVideo(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/video/" + checksum,
                "",
                MediaStatus.PENDING
        );
    }

    private ImageMedia generateMockImage(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/images/" + checksum
        );
    }
}
