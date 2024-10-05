package com.project.optics.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PasswordService {

    private static final String PASSWORD_FILE = "data/p.txt";

    public boolean isValidPassword(String password) {
        try {
            if (!Files.exists(Paths.get(PASSWORD_FILE))) {
                createPasswordFile("admin");
            }
            String filePass = readPasswordFromFile();
            return filePass.equals(password);
        } catch (IOException e) {
            return false;
        }
    }

    public void updatePassword(String newPassword) {
        try {
            Files.writeString(Paths.get(PASSWORD_FILE), newPassword);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update password", e);
        }
    }

    private void createPasswordFile(String password) throws IOException {
        Files.writeString(Paths.get(PASSWORD_FILE), password);
    }

    private String readPasswordFromFile() throws IOException {
        return Files.readString(Paths.get(PASSWORD_FILE)).trim();
    }
}
