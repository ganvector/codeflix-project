package com.codeflix.admin.catalogo.application.video.delete;

import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase{

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String anId) {
        try {
          videoGateway.deleteById(VideoID.from(anId));
        } catch (final Throwable t) {
            throw InternalErrorException.with("An error on delete video was observed [videoId:%s]".formatted(anId), t);
        }
    }
}
