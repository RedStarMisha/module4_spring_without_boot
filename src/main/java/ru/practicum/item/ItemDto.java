package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.urlretriever.UrlMetaDto;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto implements Serializable {
    private Long id;
    private Long userId;
    private String url;
    private Set<String> tags;
    private UrlMetaDto urlMetaDto;

    public ItemDto(Long id, Long userId, String url, Set<String> tags) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.tags = tags;
    }
}
