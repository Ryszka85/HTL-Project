package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.scheduling.annotation.Async;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Entity(name = "image")
public class ImageEntity implements Serializable {
    private static final long serialVersionUID = -5568948303816165336L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "image_id")
    private String imageId;
    private String name;
    private String path;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private UserEntity userEntity;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tagged_image",
            joinColumns = {@JoinColumn(name = "id_image")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")})
    private List<TagEntity> tags;
    @ManyToMany(mappedBy = "likes", fetch = FetchType.LAZY)
    private List<UserEntity> userLikesList;
    private int downloaded;
    @Column(name = "url_reference")
    private String urlReference;
    @Column(name = "is_public")
    private boolean isPublic;
    @Column(name = "upload_Date")
    private Date uploadDate;
    @JsonBackReference
    @OneToMany(mappedBy = "imageEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImageDetailsEntity> imageDetailsEntities;


    public ImageEntity() {
    }


    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public List<ImageDetailsEntity> getImageDetailsEntities() {
        return imageDetailsEntities;
    }

    public void setImageDetailsEntities(List<ImageDetailsEntity> imageDetailsEntities) {
        this.imageDetailsEntities = imageDetailsEntities;
    }

    public String getUrlReference() {
        return urlReference;
    }

    public void setUrlReference(String urlReference) {
        this.urlReference = urlReference;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public List<UserEntity> getUserLikesList() {
        return userLikesList;
    }

    public void setUserLikesList(List<UserEntity> userLikesList) {
        this.userLikesList = userLikesList;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }
}
