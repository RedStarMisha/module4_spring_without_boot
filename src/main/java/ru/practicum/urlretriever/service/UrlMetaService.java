package ru.practicum.urlretriever.service;

import ru.practicum.item.Item;
import ru.practicum.urlretriever.ItemRetrieverException;
import ru.practicum.urlretriever.UrlMetaDto;

public interface UrlMetaService {

    UrlMetaDto saveNewUrlMeta(Item item);

    UrlMetaDto findByItemId(Item item);
}
