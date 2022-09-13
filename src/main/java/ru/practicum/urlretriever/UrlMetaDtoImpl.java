package ru.practicum.urlretriever;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;


@Builder(toBuilder = true)
@Value
public class UrlMetaDtoImpl implements UrlMetaDto {

    String normalUrl;
    String resolvedUrl;
    String mimeType;
    String title;
    boolean hasImage;
    boolean hasVideo;
    Instant dateResolved;
}
