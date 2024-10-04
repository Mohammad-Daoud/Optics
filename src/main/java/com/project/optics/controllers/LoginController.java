package com.project.optics.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

@Controller
public class LoginController {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Handle login form submission
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("loggedIn", true);
            return "redirect:/clients";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    // Show password change page
    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "change-password";
    }

    // Handle password change
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        if (!adminPassword.equals(currentPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "change-password";
        }

        updatePasswordInProperties(newPassword);
        model.addAttribute("success", "Password changed successfully");
        return "change-password";
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // Handle database backup
    @PostMapping("/backup-db")
    @ResponseBody
    public ResponseEntity<Resource> backupDatabase() throws IOException {
        // Path to backup location
        Path backupPath = Paths.get("backup/opticsdb-backup.mv.db");

        // Copy the database file to the backup location
        Files.copy(Paths.get("data/opticsdb.mv.db"), backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Prepare the resource for download
        Resource fileResource = new PathResource(backupPath);

        // Set the content-disposition header to trigger download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=opticsdb-backup.mv.db");

        // Return the file as a downloadable response
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

    // Handle database restore
    @PostMapping("/restore-db")
    public String restoreDatabase(@RequestParam("dbFile") MultipartFile dbFile, Model model) throws IOException {

        Path restorePath = Paths.get("data/opticsdb.mv.db");
        Files.copy(dbFile.getInputStream(), restorePath, StandardCopyOption.REPLACE_EXISTING);

        // Simulate application restart after restore
        model.addAttribute("restoreSuccess", true);
        System.exit(0);

        return "redirect:/admin-actions?restoreSuccess=true";
    }

    // Method to update the password in the properties file
    private void updatePasswordInProperties(String newPassword) {
        Properties properties = new Properties();
        try (OutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
            // Load existing properties
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

            // Update the password
            properties.setProperty("admin.password", newPassword);

            // Save the updated properties back to the file
            properties.store(output, null);

            // Update the in-memory password variable
            adminPassword = newPassword;

        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    // Show admin actions page
    @GetMapping("/admin-actions")
    public String showAdminActions(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "admin-actions";
    }

    @GetMapping("/exit")
    public void exit(HttpSession session) {
        session.invalidate();
        System.exit(0);
    }
}
