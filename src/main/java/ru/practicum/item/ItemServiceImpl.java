package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.urlretriever.UrlMetaDto;
import ru.practicum.urlretriever.service.UrlMetaService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UrlMetaService urlMetaService;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = repository.findByUserId(userId);
        return ItemMapper.mapToItemDto(userItems);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Optional<Item> maybeExistItem = repository.findByUserIdAndUrl(userId, itemDto.getUrl());
        Item item;
        UrlMetaDto urlMetaDto;
        if (maybeExistItem.isEmpty()) {
            item = repository.save(ItemMapper.mapToItem(itemDto, userId));
            urlMetaDto = urlMetaService.saveNewUrlMeta(item);
        } else {
            item = maybeExistItem.get();
            item.getTags().addAll(itemDto.getTags());
            repository.save(item);
            urlMetaDto = urlMetaService.findByItemId(item);
        }
        return ItemMapper.mapToItemDto(item, urlMetaDto);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public List<ItemInfoWithUrlState> getUserItemStates(long userId) {
        return repository.findAllByUserIdWithUrlState(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(long userId, Set<String> tags) {
        BooleanExpression byUserId = QItem.item.userId.eq(userId);
        BooleanExpression byAnyTag = QItem.item.tags.any().in(tags);
        Iterable<Item> foundItems = repository.findAll(byUserId.and(byAnyTag));
        return ItemMapper.mapToItemDto(foundItems);
    }

    public List<ItemDto> getItems(GetItemRequest req) {

    }
}
