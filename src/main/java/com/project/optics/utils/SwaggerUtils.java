package com.project.optics.utils;

import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.HttpURLConnection;
import java.net.URL;

public class SwaggerUtils {
    public static void openUIWindow(ApplicationContext context) {
        String url = NetworkUtil.getUrl(context);
        System.setProperty("java.awt.headless", "false"); //Disables headless
        // Create the frame with larger size
        JFrame frame = new JFrame("Optics System");
        frame.setSize(600, 250); // Increased the size to make the window bigger
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a panel with padding
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

        // Warning message label
        JLabel warningLabel = new JLabel("<html>If you close this window, the system will shut down.<br>The system will remain running until this window is closed or you click the 'Shutdown System' button.</html>");
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        warningLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set bold and slightly smaller font
        warningLabel.setForeground(Color.RED); // Make the warning text red for emphasis

        // Panel for URL and copy button
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
        urlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for displaying the URL
        JTextField urlField = new JTextField("System running at: " + url);
        urlField.setFont(new Font("Arial", Font.BOLD, 14)); // Make the text bold
        urlField.setEditable(false); // Make it non-editable but selectable
        urlField.setBackground(null); // Make background transparent to blend with panel
        urlField.setBorder(null); // Remove the border for a cleaner look
        urlField.setHorizontalAlignment(JTextField.CENTER); // Center the text

        // Copy button to copy the URL to clipboard
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(new Font("Arial", Font.BOLD, 12)); // Set font for the button
        copyButton.setPreferredSize(new Dimension(80, 30)); // Set size of the button

        // Action to copy the URL to clipboard
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(url);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            JOptionPane.showMessageDialog(frame, "URL copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add URL field and copy button to the URL panel
        urlPanel.add(urlField);
        urlPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between URL and button
        urlPanel.add(copyButton);

        // Button to shut down the system
        JButton shutdownButton = new JButton("Shutdown System");
        shutdownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shutdownButton.setPreferredSize(new Dimension(220, 45)); // Set larger size for the button
        shutdownButton.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for the button
        shutdownButton.setBackground(new Color(220, 53, 69)); // Bootstrap-like danger color for shutdown
        shutdownButton.setForeground(Color.WHITE);

        // Add action listener to the button to shut down the system
        shutdownButton.addActionListener(e -> {
            try {
                sendExitRequest(url + "/exit"); // Call the /exit endpoint
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error during shutdown: " + ex.getMessage(), "Shutdown Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the main panel
        panel.add(warningLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing between elements
        panel.add(urlPanel); // Add the URL panel with the copy button
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Add more spacing
        panel.add(shutdownButton);

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);
    }


    private static void sendExitRequest(String exitUrl) throws Exception {
        URL url = new URL(exitUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            System.exit(0); // Exit the application after successful shutdown
        } else {
            throw new RuntimeException("Failed to shut down. Response code: " + responseCode);
        }
    }
}
