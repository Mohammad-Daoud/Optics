package com.project.optics.controllers;

import com.project.optics.services.FileService;
import com.project.optics.services.DatabaseService;
import com.project.optics.services.PasswordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.IOException;
import java.nio.file.Path;

@Controller
public class LoginController {

    private final PasswordService passwordService;
    private final FileService fileService;
    private final DatabaseService databaseService;

    @Value("${admin.username}")
    private String adminUsername;

    public LoginController(PasswordService passwordService, FileService fileService, DatabaseService databaseService) {
        this.passwordService = passwordService;
        this.fileService = fileService;
        this.databaseService = databaseService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session, Model model) {
        if (adminUsername.equals(username) && passwordService.isValidPassword(password)) {
            session.setAttribute("loggedIn", true);
            return "redirect:/clients";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
        if (!passwordService.isValidPassword(currentPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "change-password";
        }
        passwordService.updatePassword(newPassword);
        model.addAttribute("success", "Password changed successfully");
        return "change-password";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @PostMapping("/backup-db")
    @ResponseBody
    public ResponseEntity<Resource> backupDatabase() {
        try {
            Path backupFile = databaseService.performBackup();
            HttpHeaders headers = fileService.prepareDownloadHeaders("opticsdb-backup.zip");
            Resource fileResource = fileService.loadFileAsResource(backupFile);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/restore-db")
    public String restoreDatabase(@RequestParam("dbFile") MultipartFile dbFile, Model model) {
        try {
            databaseService.restoreDatabase(dbFile);
            model.addAttribute("restoreSuccess", true);
            System.exit(0); // Simulate application restart
        } catch (IOException e) {
            model.addAttribute("error", "Failed to restore database.");
        }
        return "redirect:/admin-actions?restoreSuccess=true";
    }

    @GetMapping("/admin-actions")
    public String showAdminActions(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        return "admin-actions";
    }

    @GetMapping("/exit")
    public void exit(HttpSession session) {
        session.invalidate();
        System.exit(0);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedIn") != null;
    }
}
