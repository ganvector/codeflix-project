package com.codeflix.admin.catalogo.domain.video;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID videoID, Resource resource);

    ImageMedia storeImage(VideoID videoID, Resource resource);

    void clearResources(VideoID videoID);
}
