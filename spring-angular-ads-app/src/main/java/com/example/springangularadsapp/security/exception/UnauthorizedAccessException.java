package com.example.springangularadsapp.security.exception;

import com.example.springangularadsapp.security.models.ERole;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(ERole privileges) {
        super("You must possess at least " + privileges + " access privileges.");
    }
}
