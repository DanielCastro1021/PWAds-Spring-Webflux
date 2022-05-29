package com.example.reactivepwads.security.exception;


import com.example.reactivepwads.security.model.Role;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(Role privileges) {
        super("You must possess at least " + privileges + " access privileges.");
    }
}
