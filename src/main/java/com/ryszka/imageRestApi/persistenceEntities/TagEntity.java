package com.ryszka.imageRestApi.persistenceEntities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Entity(name = "tags")
public class TagEntity implements Serializable {
    private static final long serialVersionUID = -303065209551727796L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tag_id")
    private String tagId;
    private String tag;
    @ManyToMany(mappedBy = "tags",cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<ImageEntity> imageEntities;

    public TagEntity() {
    }

    public TagEntity(String tag) {
        this.tag = tag;
        this.tagId = UUID.randomUUID().toString();
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public List<ImageEntity> getImageEntities() {
        Logger logger = LoggerFactory.getLogger(TagEntity.class);
        logger.info("Starting [ getImageEntities ] query ...");
        return imageEntities;
    }

    public void setImageEntities(List<ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }
}
