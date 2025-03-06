package com.codeflix.admin.catalogo.application.video.create;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;
import com.codeflix.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase{

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultCreateVideoUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final Rating aRating = Rating.from(aCommand.rating())
                .orElse(null);
        final var launchedAt = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final Set<CategoryID> categoryIds = new HashSet<>(aCommand.categories().stream().map(CategoryID::load).toList());
        final Set<GenreID> genreIds = new HashSet<>(aCommand.genres().stream().map(GenreID::load).toList());
        final Set<CastMemberID> castMemberIds = new HashSet<>(aCommand.castMembers().stream().map(CastMemberID::load).toList());

        final var notification = Notification.create();
        notification.append(validateCategories(categoryIds));
        notification.append(validateGenres(genreIds));
        notification.append(validateCastMembers(castMemberIds));

        Video video = Video.create(
                aCommand.title(),
                aCommand.description(),
                launchedAt,
                aCommand.duration(),
                aRating,
                aCommand.opened(),
                aCommand.published(),
                categoryIds,
                genreIds,
                castMemberIds
        );

        video.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not create a video", notification);
        }

        create(aCommand, video);

        return CreateVideoOutput.from(video);
    }

    private Supplier<DomainException> invalidRating(final String rating) {
        return () -> DomainException.raise(new Error("Rating not found %s".formatted(rating)));
    }

    private ValidationHandler validateCategories(final Set<CategoryID> categoryIds) {
        return validateAggregate("categories", categoryIds, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> genreIds) {
        return validateAggregate("genres", genreIds, genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> castMemberIds) {
        return validateAggregate("cast members", castMemberIds, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregateName,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ){
        final Notification notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final List<T> existingIds = existsByIds.apply(ids);

        if (ids.size() != existingIds.size()) {
            final List<T> missingIds = new ArrayList<>(ids);
            missingIds.removeAll(existingIds);

            final String missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregateName, missingIdsMessage)));
        }

        return notification;
    }

    private Video create(final CreateVideoCommand command, final Video aVideo) {
        final var videoId = aVideo.getId();
        try {

            final var audioVideoMedia = command.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(videoId, it))
                    .orElse(null);

            final var trailerMedia = command.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(videoId, it))
                    .orElse(null);

            final var bannerMedia = command.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(videoId, it))
                    .orElse(null);

            final var thumbnailMedia = command.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(videoId, it))
                    .orElse(null);

            final var thumbnailHalfMedia = command.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(videoId, it))
                    .orElse(null);

            aVideo.setVideo(audioVideoMedia)
                    .setTrailer(trailerMedia)
                    .setBanner(bannerMedia)
                    .setThumbnail(thumbnailMedia)
                    .setThumbnailHalf(thumbnailHalfMedia);

            return this.videoGateway.create(aVideo);
        } catch (final Throwable t) {
            this.mediaResourceGateway.clearResources(videoId);
            throw InternalErrorException.with("An error on create video was observed [videoId:%s]".formatted(videoId.getValue()), t);
        }
    }
}
