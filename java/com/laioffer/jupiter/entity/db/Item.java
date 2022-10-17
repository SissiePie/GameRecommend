package com.laioffer.jupiter.entity.db;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties is a class-level annotation that marks a property
// or a list of properties that Jackson will ignore.
@JsonInclude(JsonInclude.Include.NON_NULL)
//We can use @JsonInclude to exclude properties with empty/null/default values.
public class Item implements Serializable {
    //Serializable
    //here use Serializable to transfer an object in java to "String"/"Integer"/"Long" etc. to store in the database;
    private static final long serialVersionId = 1L;
    // help to match if the version is different;  可能会有不同版本。
    @Id
    @JsonProperty("id")
    //We can add the @JsonProperty annotation to indicate the property name in JSON.
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("url")
    private String url;

    @Column(name = "thumbnail_url")
    @JsonProperty("thumbnail_ulr")
    private String thumbnailUrl;

    @Column(name ="game_id")
    @JsonProperty("game_id")
    private String gameId;

    @Column(name ="broadcaster_name")
    @JsonProperty("broadcaster_name")
    @JsonAlias({ "user_name" })
    //@JasonAlias defines one or more alternative names for a property during deserialization.
    private String broadcasterName;

    @ManyToMany(mappedBy = "itemSet")
    private Set<User> userSet = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    // define Enumerated type
    @JsonProperty("item_type")
    private ItemType itemType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public void setBroadcasterName(String broadcasterName) {
        this.broadcasterName = broadcasterName;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
