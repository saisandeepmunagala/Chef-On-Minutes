package com.chefonminutes.exception;

/** Thrown when a caller tries to modify a resource they don't own (e.g. another chef's menu). */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
