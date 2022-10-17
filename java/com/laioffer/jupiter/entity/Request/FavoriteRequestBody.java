package com.laioffer.jupiter.entity.Request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laioffer.jupiter.dao.FavoriteDao;
import com.laioffer.jupiter.entity.db.Item;

public class FavoriteRequestBody {
    //transfer the json data to Json object;
    private final Item item;
//item is favorite item
    @JsonCreator
    // transfer string into Object
    // is used to fine tune the constructor or factory method used in deserialization.
    // We'll be using @JsonProperty as well to achieve the same.
    // In the example below, we are matching a json with different format to our class
    // by defining the required property names.
    public FavoriteRequestBody(@JsonProperty("favorite") Item item){
        this.item = item;
    }

    public Item getItem(){
        return item;
    }
}
