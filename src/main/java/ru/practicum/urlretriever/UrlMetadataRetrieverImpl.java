package ru.practicum.urlretriever;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

@Component
public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {

//    @Value("${cl.timeout}")
//    private String timeout;
    @Override
    public UrlMetaDto retrieve(String urlString) throws ItemRetrieverException {
        final URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            // Если адрес не соответствует правилам URI адресов, то генерируем исключение.
            throw new ItemRetrieverException("The URL is malformed: " + urlString);
        }
        //HttpResponse.BodyHandlers.discarding() обработка запроса без тела
        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());
        System.out.println(resp);

        String contentType = resp.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetaDtoImpl result;

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
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(1))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.noBody())
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            return client.send(request, responseBodyHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private UrlMetaDtoImpl handleText(URI url) {
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
        return UrlMetaDtoImpl.builder()
                .title(doc.title())
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private UrlMetaDtoImpl handleVideo(URI url) {
        //заполняем поля для случая, когда страница содержит видео
        String name = new File(url).getName();
        return UrlMetaDtoImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetaDtoImpl handleImage(URI url) {
        //заполняем поля для случая, когда страница содержит изображение
        String name = new File(url).getName();
        return UrlMetaDtoImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }
}
