package com.codeflix.admin.catalogo.application.video.retrieve.get;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.video.Video;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase{

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anId) {
        final var videoId = VideoID.from(anId);
        return videoGateway.findById(videoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.raise(Video.class, videoId));
    }
}
