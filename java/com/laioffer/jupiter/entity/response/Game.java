package com.laioffer.jupiter.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Game.Builder.class)
public class Game {
    // the response data fields we get from the twitch
    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("box_art_url")
    // match the camelcase field name
    private final String boxArtUrl;

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getBoxArtUrl(){
        return  boxArtUrl;
    }

    // construct the game class in the Builder
    // we just set the fields which we want in the Builder.

    private Game(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.boxArtUrl = builder.boxArtUrl;
    }
        // builder pattern
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Builder{
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("box_art_url")
        private String boxArtUrl;

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder boxArtUrl(String boxArtUrl){
            this.boxArtUrl = boxArtUrl;
            return this;
        }

        public Game build(){
            return new Game(this);
        }
    }

}
