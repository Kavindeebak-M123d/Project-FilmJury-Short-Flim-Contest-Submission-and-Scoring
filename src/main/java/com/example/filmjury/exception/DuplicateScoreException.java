package com.example.filmjury.exception;

public class DuplicateScoreException extends RuntimeException {
    public DuplicateScoreException(String message) {
        super(message);
    }
}
