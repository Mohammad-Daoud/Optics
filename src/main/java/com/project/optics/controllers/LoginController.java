package com.project.optics.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    @PostMapping("/backup-db")
    @ResponseBody
    public ResponseEntity<Resource> backupDatabase() {
        try {
            // Perform the H2 database backup
            String backupFilePath = performH2Backup();

            // Prepare the headers for downloading the backup file
            HttpHeaders headers = prepareDownloadHeaders("opticsdb-backup.zip");

            // Return the backup file as a downloadable response
            Resource fileResource = new PathResource(backupFilePath);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } catch (Exception e) {
            // Handle any exception that occurs during backup
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // You can return a meaningful error response here
        }
    }

    private String performH2Backup() throws SQLException, IOException {
        // Path to the backup file
        String backupFilePath = "backup/opticsdb-backup.zip";

        // Ensure the backup directory exists
        Path backupDirectory = Paths.get("backup");
        if (!Files.exists(backupDirectory)) {
            Files.createDirectories(backupDirectory);
        }

        // Backup the H2 database
        String dbUrl = "jdbc:h2:file:./data/opticsdb;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE";  // Adjust your DB URL if necessary
        Connection conn = DriverManager.getConnection(dbUrl, "root", "");  // Use appropriate credentials
        Statement stmt = conn.createStatement();
        stmt.execute("BACKUP TO '" + backupFilePath + "'");  // Use H2's backup command
        stmt.close();
        conn.close();

        return backupFilePath;
    }

    private HttpHeaders prepareDownloadHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return headers;
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
