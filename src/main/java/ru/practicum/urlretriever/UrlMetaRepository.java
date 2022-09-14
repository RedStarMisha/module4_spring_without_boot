package ru.practicum.urlretriever;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.item.Item;

import java.util.List;

public interface UrlMetaRepository extends JpaRepository<UrlMeta, Long>, QuerydslPredicateExecutor<UrlMeta> {

    UrlMeta findByItem_Id(long id);

}
