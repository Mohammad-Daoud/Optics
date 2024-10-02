package com.project.optics.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long clientId) {
        super("Client with ID " + clientId + " not found.");
    }
}
