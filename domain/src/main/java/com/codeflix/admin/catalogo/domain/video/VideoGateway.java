package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video video);

    Video update(Video video);

    void deleteById(VideoID videoId);

    Optional<Video> findById(VideoID videoId);

    Pagination<Video> findAll(VideoSearchQuery searchQuery);
}
