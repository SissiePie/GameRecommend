package com.laioffer.jupiter.entity.Request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// For store the login information sent from frontend
public class LoginRequestBody {
    private final String userId;
    private final String password;

    @JsonCreator
   // @JsonCreator is used to fine tune the constructor or factory method used in deserialization.
    // We'll be using @JsonProperty as well to achieve the same.
    // In the example below, we are matching json with different format to our class
    // by defining the required property names.
    public LoginRequestBody(@JsonProperty("user_id") String userId, @JsonProperty("password") String password){
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}

