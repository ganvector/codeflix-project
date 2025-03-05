package com.codeflix.admin.catalogo.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioVideoMediaTest {
    @Test
    public void givenValidParams_whenCreatingAudioVideoMedia_shouldReturnAudioVideoMediaInstance() {
        // given
        final var expectedChecksum = "checksum";
        final var expectedName = "name.mp4";
        final var expectedRawLocation = "/videos";
        final var expectedEncodedLocation = "/videos";
        final var expectedStatus = MediaStatus.COMPLETED;

        // when
        final var audioVideoMedia = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        Assertions.assertNotNull(audioVideoMedia);
        assertEquals(expectedChecksum, audioVideoMedia.getChecksum());
        assertEquals(expectedName, audioVideoMedia.getName());
        assertEquals(expectedRawLocation, audioVideoMedia.getRawLocation());
        assertEquals(expectedEncodedLocation, audioVideoMedia.getEncodedLocation());
        assertEquals(expectedStatus, audioVideoMedia.getStatus());
    }

    @Test
    public void givenTwoInstancesWithSameChecksumAndLocation_whenCallingEquals_shouldReturnTrue() {
        // given
        final var expectedChecksum = "checksum";
        final var expectedRawLocation = "/videos";

        // when
        final var audioVideoMedia1 = AudioVideoMedia.with(expectedChecksum, "video1.mp4", expectedRawLocation, "encodedLocation1", MediaStatus.COMPLETED);
        final var audioVideoMedia2 = AudioVideoMedia.with(expectedChecksum, "video2.mp4", expectedRawLocation, "encodedLocation2", MediaStatus.PENDING);

        // then
        Assertions.assertEquals(audioVideoMedia1, audioVideoMedia2);
        Assertions.assertNotSame(audioVideoMedia1, audioVideoMedia2);
    }

    @Test
    public void givenInvalidParams_whenCreatingAnAudioVideoMediaInstance_shouldThrowError() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            AudioVideoMedia.with(null, "video.mp4", "/videos", "/encodings", MediaStatus.PROCESSING);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            AudioVideoMedia.with("checksum", null, "/videos", "/encodings", MediaStatus.PROCESSING);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            AudioVideoMedia.with("checksum", "video.mp4", null, "/encodings", MediaStatus.PROCESSING);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            AudioVideoMedia.with("checksum", "video.mp4", "/videos", null, MediaStatus.PROCESSING);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            AudioVideoMedia.with("checksum", "video.mp4", "/videos", "/encodings",null);
        });
    }
}