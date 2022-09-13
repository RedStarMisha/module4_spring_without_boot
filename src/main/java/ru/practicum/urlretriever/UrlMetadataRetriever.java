package ru.practicum.urlretriever;

public interface UrlMetadataRetriever {
    UrlMetaDto retrieve(String urlString) throws ItemRetrieverException;
}
