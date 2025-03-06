package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    private Video(
            final VideoID identifier,
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean opened,
            final boolean published,
            final Instant createdAt,
            final Instant updatedAt,
            final ImageMedia banner,
            final ImageMedia thumbnail,
            final ImageMedia thumbnailHalf,
            final AudioVideoMedia trailer,
            final AudioVideoMedia video,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        super(identifier);
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public Video setVideo(final AudioVideoMedia audioVideoMedia) {
        this.video = audioVideoMedia;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailerMedia) {
        this.trailer = trailerMedia;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setBanner(final ImageMedia bannerMedia) {
        this.banner = bannerMedia;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnailMedia) {
        this.thumbnail = thumbnailMedia;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalfMedia) {
        this.thumbnailHalf = thumbnailHalfMedia;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video update(
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean opened,
            final boolean published,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
        this.updatedAt = Instant.now();
        return this;
    }

    public static Video create(
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final Rating rating,
            final boolean opened,
            final boolean published,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        final Instant now = Instant.now();
        final VideoID anId = VideoID.generateUnique();
        return new Video(
                anId,
                title,
                description,
                launchedAt,
                duration,
                rating,
                opened,
                published,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                castMembers
        );
    }

    public static Video from(final Video aVideo) {
        return new Video(
                aVideo.id,
                aVideo.title,
                aVideo.description,
                aVideo.launchedAt,
                aVideo.duration,
                aVideo.rating,
                aVideo.opened,
                aVideo.published,
                aVideo.createdAt,
                aVideo.updatedAt,
                aVideo.banner,
                aVideo.thumbnail,
                aVideo.thumbnailHalf,
                aVideo.trailer,
                aVideo.video,
                new HashSet<>(aVideo.categories),
                new HashSet<>(aVideo.genres),
                new HashSet<>(aVideo.castMembers)
        );
    }
}
