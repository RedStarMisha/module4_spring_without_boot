package ru.practicum.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.urlretriever.UrlMetaDto;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static Item mapToItem(ItemDto itemDto, long userId) {
        Item item = new Item();
        item.setUserId(userId);
        item.setUrl(itemDto.getUrl());
        item.setTags(itemDto.getTags());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getUserId(),
                item.getUrl(),
                item.getTags() != null ? new HashSet<>(item.getTags()) : Collections.emptySet()
        );
    }

    public static ItemDto mapToItemDto(Item item, UrlMetaDto urlMetaDto) {
        return new ItemDto(
                item.getId(),
                item.getUserId(),
                item.getUrl(),
                item.getTags() != null ? new HashSet<>(item.getTags()) : Collections.emptySet(),
                urlMetaDto
        );
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }
}
