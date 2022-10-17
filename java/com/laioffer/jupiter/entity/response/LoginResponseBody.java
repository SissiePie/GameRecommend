package com.laioffer.jupiter.entity.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseBody {
    // we will return name and id
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("name")
    private  final String name;

    @JsonCreator
    public LoginResponseBody( String userId, String name ) {
     this.name = name;
     this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
