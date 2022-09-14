package ru.practicum.urlretriever.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Pageable;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.urlretriever.UrlMetaDto;

import java.util.List;

public interface UrlMetaService {

    UrlMetaDto saveNewUrlMeta(Item item);

    UrlMetaDto findByItemId(Item item);

    List<ItemDto> findItemByParameters(BooleanExpression booleanExpression, Pageable pageable);
}
