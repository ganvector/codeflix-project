package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallingNewVideo_shouldInstantiateVideo () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        // when
        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertEquals(video.getCreatedAt(), video.getUpdatedAt());
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingUpdateVideo_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                "Shrek 2",
                "Shrek is love",
                Year.of(2004),
                0.0,
                Rating.AGE_10,
                true,
                true,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualVideo = Video.from(originalVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertNotNull(actualVideo.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == actualVideo.getCategories().size() && expectedCategories.containsAll(actualVideo.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == actualVideo.getGenres().size() && expectedGenres.containsAll(actualVideo.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == actualVideo.getCastMembers().size() && expectedCastMembers.containsAll(actualVideo.getCastMembers()));
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingSetVideo_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var videoMedia = AudioVideoMedia.with(
                "123", "video.mp4", "/123/tests", "123/tests", MediaStatus.COMPLETED
        );
        // when

        final var video = Video.from(originalVideo).setVideo(videoMedia);

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(video.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertEquals(videoMedia, video.getVideo().get());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingSetTrailer_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var trailerMedia = AudioVideoMedia.with(
                "456", "trailer.mp4", "/123/trailers", "123/trailers", MediaStatus.PENDING
        );
        // when

        final var video = Video.from(originalVideo).setTrailer(trailerMedia);

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(video.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertEquals(trailerMedia, video.getTrailer().get());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingSetBanner_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var bannerMedia = ImageMedia.with(
                "789", "banner.jpg", "/123/banners"
        );
        // when

        final var video = Video.from(originalVideo).setBanner(bannerMedia);

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(video.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertEquals(bannerMedia, video.getBanner().get());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingSetThumbnail_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var thumbnailMedia = ImageMedia.with(
                "011", "thumbnail.jpg", "/123/thumbnails"
        );
        // when

        final var video = Video.from(originalVideo).setThumbnail(thumbnailMedia);

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(video.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertEquals(thumbnailMedia, video.getThumbnail().get());
        Assertions.assertTrue(video.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidParams_whenCallingSetThumbnailHalf_shouldReturnVideoUpdated () {
        //given
        final var expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var originalVideo = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var thumbnailHalfMedia = ImageMedia.with(
                "012", "thumbnail-half.jpg", "/123/thumbnail"
        );
        // when

        final var video = Video.from(originalVideo).setThumbnailHalf(thumbnailHalfMedia);

        // then
        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPublished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertTrue(originalVideo.getUpdatedAt().isBefore(video.getUpdatedAt()));
        Assertions.assertTrue(expectedCategories.size() == video.getCategories().size() && expectedCategories.containsAll(video.getCategories()));
        Assertions.assertTrue(expectedGenres.size() == video.getGenres().size() && expectedGenres.containsAll(video.getGenres()));
        Assertions.assertTrue(expectedCastMembers.size() == video.getCastMembers().size() && expectedCastMembers.containsAll(video.getCastMembers()));
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertEquals(thumbnailHalfMedia, video.getThumbnailHalf().get());


        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }
}
