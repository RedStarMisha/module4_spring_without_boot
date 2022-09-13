package ru.practicum.urlretriever;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {
    @Override
    public UrlMetadata retrieve(String urlString) throws ItemRetrieverException {
        final URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            // Если адрес не соответствует правилам URI адресов, то генерируем исключение.
            throw new ItemRetrieverException("The URL is malformed: " + urlString);
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
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request;
        switch (method){
            case "HEAD":
                request = HttpRequest.newBuilder()
                        .uri(url)
                        .method(method, HttpRequest.BodyPublishers.noBody())
                        .version(HttpClient.Version.HTTP_1_1)
                        .build();
            case "GET":

        }
        HttpRequest requestForSave = HttpRequest.newBuilder()
                .method
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(postUri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(requestForSave , HttpResponse.BodyHandlers.ofString());
    }

    private UrlMetadataImpl handleText(URI url) {
        //заполняем поля для случая, когда страница содержит текст (в том числе html)
        // Отправим get-запрос, чтобы получить содержимое
        HttpResponse<String> resp = connect(url, "GET", HttpResponse.BodyHandlers.ofString());

        // воспользуемся библиотекой Jsoup для парсинга содержимого
        Document doc = Jsoup.parse(resp.body());

        // ищем в полученном документе html-тэги, говорящие, что он
        // содержит видео или аудио информацию
        Elements imgElements = doc.getElementsByTag("img");
        Elements videoElements = doc.getElementsByTag("video");

        // добавляем полученные данные в ответ. В том числе находим заголовок
        // полученной страницы.
        return UrlMetadataImpl.builder()
                .title(doc.title())
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private UrlMetadataImpl handleVideo(URI url) {
        //заполняем поля для случая, когда страница содержит видео
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetadataImpl handleImage(URI url) {
        //заполняем поля для случая, когда страница содержит изображение
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }
}
