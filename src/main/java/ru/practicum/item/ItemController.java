package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Later-User-Id") long userId,
                             @RequestParam(required = false) Set<String> tags) {
        if(tags == null || tags.isEmpty()) {
            return itemService.getItems(userId);
        } else {
            return itemService.getItems(userId, tags);
        }
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Later-User-Id") long userId,
                             @RequestParam(defaultValue = "unread") String state,
                             @RequestParam(defaultValue = "all") String contentType,
                             @RequestParam(defaultValue = "newest") String sort,
                             @RequestParam(defaultValue = "10") int limit,
                             @RequestParam(required = false) List<String> tags) {

        return itemService.getItems(GetItemRequest.of(userId, state, contentType, sort, limit, tags));
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Later-User-Id") Long userId, @RequestBody ItemDto item) {
        System.out.println(item);
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}