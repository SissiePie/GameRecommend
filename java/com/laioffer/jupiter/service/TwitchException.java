package com.laioffer.jupiter.service;

public class   TwitchException extends RuntimeException{
    //We’ll throw this exception if there is something wrong when calling Twitch API.
    public TwitchException(String errorMessage) {
        super(errorMessage);
    }
}
