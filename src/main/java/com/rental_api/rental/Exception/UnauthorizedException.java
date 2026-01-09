// UnauthorizedException.java
package com.rental_api.rental.Exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
