package com.codeflix.admin.catalogo.application.video.retrieve.get;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.utils.CollectionUtils;
import com.codeflix.admin.catalogo.domain.video.AudioVideoMedia;
import com.codeflix.admin.catalogo.domain.video.ImageMedia;
import com.codeflix.admin.catalogo.domain.video.Resource;
import com.codeflix.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record VideoOutput(
        String id,
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        AudioVideoMedia video,
        AudioVideoMedia trailer,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoOutput from(final Video video) {
        return new VideoOutput(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getDuration(),
                video.isOpened(),
                video.isPublished(),
                video.getRating().getName(),
                CollectionUtils.toSet(video.getCategories(), CategoryID::getValue),
                CollectionUtils.toSet(video.getGenres(), GenreID::getValue),
                CollectionUtils.toSet(video.getCastMembers(), CastMemberID::getValue),
                video.getVideo().orElse(null),
                video.getTrailer().orElse(null),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbnailHalf().orElse(null),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }
}
