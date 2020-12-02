package com.ryszka.imageRestApi.viewModels.response;

import com.ryszka.imageRestApi.service.dto.TagDTO;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class UserImageViewModel implements Serializable {
    private static final long serialVersionUID = 3148099477137315634L;
    private String name, link, imageId, linkReference;
    private UserDetailsResponseModel user;
    private List<TagDTO> tags;
    /*private List<UserImageViewModel> likes;*/
    private boolean isPublic;
    private int liked, downloaded;
    private List<ImageDetailViewModel> imageDetails;
    private Date uploadDate;

    public UserImageViewModel() {
    }

    /*public UserImageViewModel(String name, String link, String imageId,
                              UserDetailsResponseModel user, List<TagDTO> tags,
                              List<UserImageViewModel> likes) {
        this.name = name;
        this.link = link;
        this.imageId = imageId;
        this.user = user;
        this.tags = tags;
        this.likes = likes;
    }*/


    public UserImageViewModel(String name,
                              String link,
                              String imageId,
                              UserDetailsResponseModel user,
                              List<TagDTO> tags,
                              String linkReference,
                              boolean isPublic,
                              int liked,
                              int downloaded,
                              List<ImageDetailViewModel> imageDetails,
                              Date uploadDate) {
        this(name, link, imageId, user, tags, linkReference, isPublic, liked, downloaded, imageDetails);
        this.uploadDate = uploadDate;
    }

    public UserImageViewModel(String name,
                              String link,
                              String imageId,
                              UserDetailsResponseModel user,
                              List<TagDTO> tags,
                              String linkReference,
                              boolean isPublic,
                              int liked,
                              int downloaded,
                              List<ImageDetailViewModel> imageDetails) {
        this(name, link, imageId, user, tags, linkReference, isPublic, liked, downloaded);
        this.imageDetails = imageDetails;
    }

    public UserImageViewModel(String name, String link, String imageId,
                              UserDetailsResponseModel user, List<TagDTO> tags,
                              String linkReference, boolean isPublic, int liked, int downloaded) {
        this(name, link, imageId, user, tags, linkReference, isPublic);
        this.liked = liked;
        this.downloaded = downloaded;
    }

    public UserImageViewModel(String name, String link, String imageId,
                              UserDetailsResponseModel user, List<TagDTO> tags,
                              String linkReference, boolean isPublic) {
        this(name, link, imageId, user, tags);
        this.linkReference = linkReference;
        this.isPublic = isPublic;
    }


    public UserImageViewModel(String name, String link, String imageId,
                              UserDetailsResponseModel user, List<TagDTO> tags) {
        this.name = name;
        this.link = link;
        this.imageId = imageId;
        this.user = user;
        this.tags = tags;
    }

    public List<ImageDetailViewModel> getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(List<ImageDetailViewModel> imageDetails) {
        this.imageDetails = imageDetails;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getLinkReference() {
        return linkReference;
    }

    public void setLinkReference(String linkReference) {
        this.linkReference = linkReference;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    /*public List<UserImageViewModel> getLikes() {
        return likes;
    }

    public void setLikes(List<UserImageViewModel> likes) {
        this.likes = likes;
    }*/

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public UserDetailsResponseModel getUser() {
        return user;
    }

    public void setUser(UserDetailsResponseModel user) {
        this.user = user;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "UserImageViewModel{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", imageId='" + imageId + '\'' +
                ", linkReference='" + linkReference + '\'' +
                ", user=" + user.getUserId() +
                ", tags=" + tags.toString() +
                ", isPublic=" + isPublic +
                ", liked=" + liked +
                ", downloaded=" + downloaded +
                '}';
    }
}
