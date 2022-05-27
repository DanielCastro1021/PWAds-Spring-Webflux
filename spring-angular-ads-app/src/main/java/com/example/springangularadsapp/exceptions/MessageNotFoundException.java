package com.example.springangularadsapp.exceptions;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String id) {
        super("Could not find message with this id : " + id + " .");
    }
}
