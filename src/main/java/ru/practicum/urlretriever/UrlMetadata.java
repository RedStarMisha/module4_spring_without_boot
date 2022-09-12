package ru.practicum.urlretriever;

import java.time.Instant;

public interface UrlMetadata {
    String getNormalUrl();
    String getResolvedUrl();
    String getMimeType();
    String getTitle();
    boolean isHasImage();
    boolean isHasVideo();
    Instant getDateResolved();
}
