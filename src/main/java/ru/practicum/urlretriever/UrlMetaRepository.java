package ru.practicum.urlretriever;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UrlMetaRepository extends JpaRepository<UrlMeta, Long>, QuerydslPredicateExecutor<UrlMeta> {

    UrlMeta findByItem_Id(long id);

    @Modifying
    @Query("update UrlMeta u set u=?2 where u.id=?1")
    UrlMeta updateUrlMeta(long urlMetaId, UrlMeta urlMeta);
}
