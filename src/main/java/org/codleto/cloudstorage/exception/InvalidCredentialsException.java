package org.codleto.cloudstorage.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {

        super("Invalid username or password");
    }
}
