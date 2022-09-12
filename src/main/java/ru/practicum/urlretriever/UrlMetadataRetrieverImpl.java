package ru.practicum.urlretriever;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.Instant;

public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {
    @Override
    public UrlMetadata retrieve(String urlString) {
        final URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            // Если адрес не соответствует правилам URI адресов, то генерируем исключение.
            throw new ItemRetrieverException("The URL is malformed: " + urlString, e);
        }
        //HttpResponse.BodyHandlers.discarding() обработка запроса без тела
        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());

        String contentType = resp.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;

        if (mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("video/*"))) {
            result = handleVideo(resp.uri());
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }

        return result.toBuilder()
                .normalUrl(urlString)
                .resolvedUrl(resp.uri().toString())
                .mimeType(mediaType.getType())
                .dateResolved(Instant.now())
                .build();
    }

    private <T> HttpResponse<T> connect(URI url,
                                        String method,
                                        HttpResponse.BodyHandler<T> responseBodyHandler) {
        //делаем запрос к данному url
    }

    private UrlMetadataImpl handleText(URI url) {
        //заполняем поля для случая, когда страница содержит текст (в том числе html)
    }

    private UrlMetadataImpl handleVideo(URI url) {
        //заполняем поля для случая, когда страница содержит видео
    }

    private UrlMetadataImpl handleImage(URI url) {
        //заполняем поля для случая, когда страница содержит изображение
    }
}
