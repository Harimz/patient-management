package com.pm.patientservice.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Patient already exists with email: " + email);
    }
}
