package ru.practicum.urlretriever;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMetaRepository extends JpaRepository<UrlMeta, Long> {

    UrlMeta findByItem_Id(long id);
}
