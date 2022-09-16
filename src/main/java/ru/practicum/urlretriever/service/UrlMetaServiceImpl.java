package ru.practicum.urlretriever.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemMapper;
import ru.practicum.urlretriever.*;

import java.util.List;
import java.util.stream.Collectors;


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

    @Override
    public List<ItemDto> findItemByParameters(BooleanExpression booleanExpression, Pageable pageable) {
        repository.
        return repository.findAll(booleanExpression, pageable).stream().map(urlMeta ->
                ItemMapper.mapToItemDto(urlMeta.getItem())).collect(Collectors.toList());
    }
}
