package ru.practicum.item;




import java.util.List;
import java.util.Set;

public class GetItemRequest {
    private Long userId;
    private State state;
    private ContentType contentType;
    private Sort sort;
    private Integer limit;
    private List<String> tags;

    public static GetItemRequest of(Long userId, String state, String contentType, String sort, Integer limit, List<String> tags) {
        GetItemRequest itemRequest = new GetItemRequest();
        itemRequest.userId = userId;
        try {
            itemRequest.state = State.valueOf(state.toUpperCase());
            itemRequest.contentType = ContentType.valueOf(contentType.toUpperCase());
            itemRequest.sort = Sort.valueOf(sort.toUpperCase());
        } catch (RuntimeException e) {
            throw new UnsupportedRequestParametersException("Неподдерживаемые параметры запроса");
        }
        itemRequest.limit = limit;
        itemRequest.tags = tags;
        return itemRequest;
    }

    public enum State {
        UNREAD,
        READ,
        ALL
    }

    public enum ContentType {
        ARTICLE,
        VIDEO,
        IMAGE,
        ALL
    }
    public enum Sort {
        NEWEST,
        OLDEST,
        TITLE,
        SITE
    }
}

