package ru.practicum.item;




import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.urlretriever.QUrlMeta;

import java.util.ArrayList;
import java.util.List;

public class GetItemRequest {
    private Long userId;
    private State state;
    private ContentType contentType;
    private Sorted sorted;
    private Integer limit;
    private List<String> tags;

    public static GetItemRequest of(Long userId, String state, String contentType, String sort, Integer limit, List<String> tags) {
        GetItemRequest itemRequest = new GetItemRequest();
        itemRequest.userId = userId;
        try {
            itemRequest.state = State.valueOf(state.toUpperCase());
            itemRequest.contentType = ContentType.valueOf(contentType.toUpperCase());
            itemRequest.sorted = Sorted.valueOf(sort.toUpperCase());
        } catch (RuntimeException e) {
            throw new UnsupportedRequestParametersException("Неподдерживаемые параметры запроса");
        }
        itemRequest.limit = limit;
        itemRequest.tags = tags;
        return itemRequest;
    }

    public ResponseParam getFindParameter(QUrlMeta urlMeta) {
        List<BooleanExpression> parameters = new ArrayList<>();
        parameters.add(urlMeta.item.userId.eq(this.userId));

        if (this.state != State.ALL) {
            BooleanExpression stateParameter = this.state == State.READ ? urlMeta.item.unread.eq(false) :
                    urlMeta.item.unread.eq(true);
            parameters.add(stateParameter);
        }

        if (this.contentType != ContentType.ALL) {
            BooleanExpression stateParameter = makeContentTypeParameter(this.contentType, urlMeta);
            parameters.add(stateParameter);
        }

        BooleanExpression howFind = parameters.stream().reduce(BooleanExpression::and).get();

        Sort sort = makeSortParameter(this.sorted);
        PageRequest pageRequest = PageRequest.of(0, this.limit, sort);

        return new ResponseParam(howFind, pageRequest);
    }

    private Sort makeSortParameter(Sorted sorted) {
        switch (sorted) {
            case NEWEST:
                return Sort.by("dateResolved").descending();
            case OLDEST:
                return Sort.by("dateResolved").ascending();
            case SITE:
                return Sort.by("url").ascending();
            case TITLE:
            default:
                return Sort.by("title").ascending();
        }
    }

    private BooleanExpression makeContentTypeParameter(ContentType contentType, QUrlMeta urlMeta) {
        switch (contentType) {
            case IMAGE:
                return urlMeta.image.isTrue().and(urlMeta.video.isFalse());
            case VIDEO:
                return urlMeta.video.isTrue().and(urlMeta.image.isFalse());
            case ARTICLE:
                return urlMeta.video.isFalse().and(urlMeta.image).isFalse();
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public class ResponseParam {
        private BooleanExpression booleanExpression;
        private Pageable pageable;
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
    public enum Sorted {
        NEWEST,
        OLDEST,
        TITLE,
        SITE
    }
}

