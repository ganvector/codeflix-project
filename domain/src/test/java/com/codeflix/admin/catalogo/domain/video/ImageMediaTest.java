package com.codeflix.admin.catalogo.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageMediaTest {

    @Test
    public void givenValidParams_whenCreatingImageMedia_shouldReturnImageMediaInstance() {
        // given
        final var expectedChecksum = "checksum";
        final var expectedName = "name.jpg";
        final var expectedLocation = "/images";

        // when
        final var imageMedia = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then
        Assertions.assertNotNull(imageMedia);
        assertEquals(expectedChecksum, imageMedia.getChecksum());
        assertEquals(expectedName, imageMedia.getName());
        assertEquals(expectedLocation, imageMedia.getLocation());
    }

    @Test
    public void givenTwoInstancesWithSameChecksumAndLocation_whenCallingEquals_shouldReturnTrue() {
        // given
        final var expectedChecksum = "checksum";
        final var expectedName = "name.jpg";
        final var expectedLocation = "/images";

        // when
        final var imageMedia1 = ImageMedia.with(expectedChecksum, "image1.jpg", expectedLocation);
        final var imageMedia2 = ImageMedia.with(expectedChecksum, "image2.jpg", expectedLocation);

        // then
        Assertions.assertEquals(imageMedia1, imageMedia2);
        Assertions.assertNotSame(imageMedia1, imageMedia2);
    }

    @Test
    public void givenInvalidParams_whenCreatingInstance_shouldError() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ImageMedia.with(null, "image1.jpg", "/images");
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            ImageMedia.with("checksum", null, "/images");
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            ImageMedia.with("checksum", "image1.jpg", null);
        });
    }
}