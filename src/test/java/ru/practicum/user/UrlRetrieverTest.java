package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.practicum.urlretriever.ItemRetrieverException;
import ru.practicum.urlretriever.UrlMetaDto;
import ru.practicum.urlretriever.UrlMetadataRetriever;
import ru.practicum.urlretriever.UrlMetadataRetrieverImpl;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitWebConfig({UrlMetadataRetrieverImpl.class})
public class UrlRetrieverTest {

    private final UrlMetadataRetriever retriever = new UrlMetadataRetrieverImpl();

    @Test
    void retrieveTest() throws ItemRetrieverException {
        UrlMetaDto urlMetaDto = retriever.retrieve("https://bit.ly/3vRVvO0");
        assertThat(urlMetaDto, Matchers.notNullValue());
        System.out.println(urlMetaDto);
        assertThat(urlMetaDto.getResolvedUrl(), Matchers.is("https://practicum.yandex.ru/"));

    }
}
