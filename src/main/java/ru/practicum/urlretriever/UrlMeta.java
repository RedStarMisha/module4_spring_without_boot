package ru.practicum.urlretriever;

import lombok.Data;
import ru.practicum.item.Item;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "urlmetadata")
@Data
public class UrlMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "mime")
    private String mime;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private boolean image;

    @Column(name = "video")
    private boolean video;

    @Column(name = "resolved")
    private Instant dateResolved;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item;
}
