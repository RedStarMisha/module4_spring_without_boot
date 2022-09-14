package ru.practicum.urlretriever.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.Item;
import ru.practicum.urlretriever.*;


@Service
@AllArgsConstructor
public class UrlMetaServiceImpl implements UrlMetaService {

    private final UrlMetaRepository repository;

    private final UrlMetadataRetriever retriever;

    @Override
    public UrlMetaDto saveNewUrlMeta(Item item) {
        try {
            UrlMetaDto urlMetaDto = retriever.retrieve(item.getUrl());
            UrlMeta urlMeta = UrlMapper.toUrlMeta(urlMetaDto, item);
            return UrlMapper.toDto(repository.save(urlMeta), item);
        } catch (ItemRetrieverException e) {
            throw new RuntimeException("Ошибка");
        }
    }

    @Override
    public UrlMetaDto findByItemId(Item item) {
        UrlMeta urlMeta = repository.findByItem_Id(item.getId());
        return UrlMapper.toDto(urlMeta, item);
    }
}
