package ru.practicum.urlretriever;

import ru.practicum.item.Item;

public class UrlMapper {

    public static UrlMetaDto toDto(UrlMeta urlMeta, Item item) {
        return UrlMetaDtoImpl.builder()
                .normalUrl(item.getUrl())
                .resolvedUrl(urlMeta.getUrl())
                .dateResolved(urlMeta.getDateResolved())
                .hasImage(urlMeta.isImage())
                .hasVideo(urlMeta.isVideo())
                .mimeType(urlMeta.getMime())
                .title(urlMeta.getTitle())
                .build();
    }

    public static UrlMeta toUrlMeta(UrlMetaDto urlMetaDto, Item item) {
        UrlMeta urlMeta = new UrlMeta();
        urlMeta.setUrl(urlMetaDto.getResolvedUrl());
        urlMeta.setTitle(urlMetaDto.getTitle());
        urlMeta.setImage(urlMetaDto.isHasImage());
        urlMeta.setVideo(urlMetaDto.isHasVideo());
        urlMeta.setMime(urlMetaDto.getMimeType());
        urlMeta.setItem(item);
        urlMeta.setDateResolved(urlMetaDto.getDateResolved());
        return urlMeta;
    }
}
