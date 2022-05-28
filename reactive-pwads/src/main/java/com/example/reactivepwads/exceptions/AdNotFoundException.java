package com.example.reactivepwads.exceptions;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException(String id) {
        super("Could not find ad with this id : " + id + " .");
    }

    public AdNotFoundException(String username, String message) {
        super("The user with the username :" + username + " " + message);
    }
}
