package com.chefonminutes.exception;

/** Thrown when an operation would violate a business invariant (illegal state transition, ownership, etc). */
public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }
}
