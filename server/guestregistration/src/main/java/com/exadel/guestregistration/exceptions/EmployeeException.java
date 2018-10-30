package com.exadel.guestregistration.exceptions;

public class EmployeeException extends Exception {
    public EmployeeException() {
    }

    public EmployeeException(String message) {
        super(message);
    }

    public EmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
