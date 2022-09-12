package ru.practicum.urlretriever;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;


@Builder(toBuilder = true)
@Value
public class UrlMetadataImpl implements UrlMetadata {

    String normalUrl;
    String resolvedUrl;
    String mimeType;
    String title;
    boolean hasImage;
    boolean hasVideo;
    Instant dateResolved;
}
