package com.mtrifonov.springsecuritystandard.exceptions;

/**
 *
 * @Mikhail Trifonov
 */
public class RoleChangeException extends RuntimeException {
    public RoleChangeException(String message) {
        super(message);
    }
}
