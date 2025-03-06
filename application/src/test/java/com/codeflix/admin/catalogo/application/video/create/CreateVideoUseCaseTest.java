package com.codeflix.admin.catalogo.application.video.create;

import com.codeflix.admin.catalogo.application.Fixture;
import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.time.Year;
import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase defaultCreateVideoUseCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway);
    }

    @Test
    public void givenAValidCommand_whenCallingCreateVideo_shouldReturnVideoId() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Videos.category().getId()
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Videos.genre().getId()
        );
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.Videos.castMember().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();

        //when
        final var output = defaultCreateVideoUseCase.execute(command);

        //then
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        verify(videoGateway).create(argThat(videoArg ->
                Objects.equals(expectedTitle, videoArg.getTitle())
                    && Objects.equals(expectedDescription, videoArg.getDescription())
                    && Objects.equals(launchYear, videoArg.getLaunchedAt())
                    && Objects.equals(expectedDuration, videoArg.getDuration())
                    && Objects.equals(expectedOpened, videoArg.isOpened())
                    && Objects.equals(expectedPublished, videoArg.isPublished())
                    && Objects.equals(expectedRating, videoArg.getRating())
                    && expectedCategories.size() == videoArg.getCategories().size()
                    && expectedCategories.containsAll(videoArg.getCategories())
                    && expectedGenres.size() == videoArg.getGenres().size()
                    && expectedGenres.containsAll(videoArg.getGenres())
                    && expectedCastMembers.size() == videoArg.getCastMembers().size()
                    && expectedCastMembers.containsAll(videoArg.getCastMembers())
                    && videoArg.getVideo().isPresent()
                    && Objects.equals(expectedVideo.getName(), videoArg.getVideo().get().getName())
                    && videoArg.getTrailer().isPresent()
                    && Objects.equals(expectedTrailer.getName(), videoArg.getTrailer().get().getName())
                    && videoArg.getBanner().isPresent()
                    && Objects.equals(expectedBanner.getName(), videoArg.getBanner().get().getName())
                    && videoArg.getThumbnail().isPresent()
                    && Objects.equals(expectedThumbnail.getName(), videoArg.getThumbnail().get().getName())
                    && videoArg.getThumbnailHalf().isPresent()
                    && Objects.equals(expectedThumbnailHalf.getName(), videoArg.getThumbnailHalf().get().getName())
        ));

    }

    @Test
    public void givenAValidCommandWithoutRelations_whenCallingCreateVideo_shouldReturnVideoID () {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();

        //when
        final var output = defaultCreateVideoUseCase.execute(command);

        //then
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        verify(videoGateway).create(argThat(videoArg ->
                Objects.equals(expectedTitle, videoArg.getTitle())
                        && Objects.equals(expectedDescription, videoArg.getDescription())
                        && Objects.equals(launchYear, videoArg.getLaunchedAt())
                        && Objects.equals(expectedDuration, videoArg.getDuration())
                        && Objects.equals(expectedOpened, videoArg.isOpened())
                        && Objects.equals(expectedPublished, videoArg.isPublished())
                        && Objects.equals(expectedRating, videoArg.getRating())
                        && expectedCategories.size() == videoArg.getCategories().size()
                        && expectedCategories.containsAll(videoArg.getCategories())
                        && expectedGenres.size() == videoArg.getGenres().size()
                        && expectedGenres.containsAll(videoArg.getGenres())
                        && expectedCastMembers.size() == videoArg.getCastMembers().size()
                        && expectedCastMembers.containsAll(videoArg.getCastMembers())
                        && videoArg.getVideo().isPresent()
                        && Objects.equals(expectedVideo.getName(), videoArg.getVideo().get().getName())
                        && videoArg.getTrailer().isPresent()
                        && Objects.equals(expectedTrailer.getName(), videoArg.getTrailer().get().getName())
                        && videoArg.getBanner().isPresent()
                        && Objects.equals(expectedBanner.getName(), videoArg.getBanner().get().getName())
                        && videoArg.getThumbnail().isPresent()
                        && Objects.equals(expectedThumbnail.getName(), videoArg.getThumbnail().get().getName())
                        && videoArg.getThumbnailHalf().isPresent()
                        && Objects.equals(expectedThumbnailHalf.getName(), videoArg.getThumbnailHalf().get().getName())
        ));
    }

    @Test
    public void givenAValidCommandWithoutResources_whenCallingCreateVideo_shouldReturnVideoID () {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Videos.category().getId()
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Videos.genre().getId()
        );
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.Videos.castMember().getId()
        );

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        //when
        final var output = defaultCreateVideoUseCase.execute(command);

        //then
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        verify(videoGateway).create(argThat(videoArg ->
                Objects.equals(expectedTitle, videoArg.getTitle())
                        && Objects.equals(expectedDescription, videoArg.getDescription())
                        && Objects.equals(launchYear, videoArg.getLaunchedAt())
                        && Objects.equals(expectedDuration, videoArg.getDuration())
                        && Objects.equals(expectedOpened, videoArg.isOpened())
                        && Objects.equals(expectedPublished, videoArg.isPublished())
                        && Objects.equals(expectedRating, videoArg.getRating())
                        && expectedCategories.size() == videoArg.getCategories().size()
                        && expectedCategories.containsAll(videoArg.getCategories())
                        && expectedGenres.size() == videoArg.getGenres().size()
                        && expectedGenres.containsAll(videoArg.getGenres())
                        && expectedCastMembers.size() == videoArg.getCastMembers().size()
                        && expectedCastMembers.containsAll(videoArg.getCastMembers())
                        && videoArg.getVideo().isEmpty()
                        && videoArg.getTrailer().isEmpty()
                        && videoArg.getBanner().isEmpty()
                        && videoArg.getThumbnail().isEmpty()
                        && videoArg.getThumbnailHalf().isEmpty()
        ));

    }

    @Test
    public void givenANullTitle_whenCallingCreateVideo_shouldReturnDomainException () {
        //given
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAEmptyTitle_whenCallingCreateVideo_shouldReturnDomainException () {
        //given
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final String expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenANullRating_whenCallingCreateVideo_shouldReturnDomainException () {
        //given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidRating_whenCallingCreateVideo_shouldReturnDomainException () {
        //given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = "invalid-rating";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenANullLaunchYear_whenCallingCreateVideo_shouldReturnDomainException () {
        //given
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer launchYear = null;
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallingCreateVideoAndCategoryDoesNotExists_shouldReturnDomainException() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var categoryId = Fixture.Videos.category().getId();
        final var expectedCategories = Set.<CategoryID>of(
                categoryId
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Videos.genre().getId()
        );
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.Videos.castMember().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(categoryId.getValue());

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommand_whenCallingCreateVideoAndGenreDoesNotExists_shouldReturnDomainException() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Videos.category().getId()
        );
        final var genreId = Fixture.Videos.genre().getId();
        final var expectedGenres = Set.<GenreID>of(
                genreId
        );
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.Videos.castMember().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(genreId.getValue());

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommand_whenCallingCreateVideoAndCastMemberDoesNotExists_shouldReturnDomainException() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Videos.category().getId()
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Videos.genre().getId()
        );
        final var castMemberId = Fixture.Videos.castMember().getId();
        final var expectedCastMembers = Set.<CastMemberID>of(
                castMemberId
        );

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(castMemberId.getValue());

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        //when
        final var output = Assertions.assertThrows(
                NotificationException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallingCreateVideoThrowsException_shouldCallClearResources() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var launchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Videos.category().getId()
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Videos.genre().getId()
        );
        final var expectedCastMembers = Set.<CastMemberID>of(
                Fixture.Videos.castMember().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var expectedErrorMessage = "An error on create video was observed [videoId:";

        final var command = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                launchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories) ,
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenThrow(new RuntimeException("error"));

        //when
        final var output = Assertions.assertThrows(
                InternalErrorException.class,
                () -> defaultCreateVideoUseCase.execute(command)
        );

        //then
        Assertions.assertNotNull(output);
        Assertions.assertTrue(output.getMessage().startsWith(expectedErrorMessage));

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(mediaResourceGateway, times(2)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(3)).storeImage(any(), any());
        verify(videoGateway, times(1)).create(any());
        verify(mediaResourceGateway, times(1)).clearResources(any());

    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer( t -> {
            final var resource = t.getArgument(1, Resource.class);
            return ImageMedia.with(UUID.randomUUID().toString(), resource.getName(), "/images");
        });
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer( t -> {
            final var resource = t.getArgument(1, Resource.class);
            return AudioVideoMedia.with(UUID.randomUUID().toString(), resource.getName(), "/videos", "", MediaStatus.PENDING);
        });
    }
}
