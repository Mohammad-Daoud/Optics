package com.project.optics;

import com.project.optics.utils.NetworkUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class OpticsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(OpticsApplication.class, args);
		String url = getUrl(context);
		openBrowser(url);
		System.setProperty("java.awt.headless", "false"); //Disables headless
		openUIWindow(url);
	}

	private static String getUrl(ApplicationContext context) {
		Environment environment = context.getBean(Environment.class);
		String port = environment.getProperty("server.port");
		String localIP = NetworkUtil.getLocalIpAddress();

		return "http://" + localIP + ":" + port;
	}

	public static void openBrowser(String url) {
		String os = System.getProperty("os.name").toLowerCase();

		try {
			if (os.contains("win")) { // Windows
				Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
			} else if (os.contains("mac")) { // MacOS
				Runtime.getRuntime().exec(new String[]{"open", url});
			} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) { // Linux/Unix
				Runtime.getRuntime().exec(new String[]{"xdg-open", url});
			} else {
				throw new UnsupportedOperationException("Unsupported operating system.");
			}
		} catch (Exception e) {
			// Ignore any exceptions during browser opening
		}
	}

	private static void openUIWindow(String url) {
		// Create the frame with larger size
		JFrame frame = new JFrame("Optics System");
		frame.setSize(600, 200); // Increased the size to make the window bigger
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Create a panel
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

		// Add label to show the URL of the system
		JLabel urlLabel = new JLabel("System running at: " + url);
		urlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		urlLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for better readability

		// Add a shutdown button
		JButton shutdownButton = new JButton("Shutdown System");
		shutdownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		shutdownButton.setPreferredSize(new Dimension(200, 50)); // Set larger size for the button
		shutdownButton.setFont(new Font("Arial", Font.BOLD, 14)); // Increase font size for the button

		// Add action listener to the button to shut down the system
		shutdownButton.addActionListener(e -> {
			try {
				sendExitRequest(url + "/exit"); // Call the /exit endpoint
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error during shutdown: " + ex.getMessage());
			}
		});

		// Add components to the panel
		panel.add(urlLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 20))); // Add more spacing between label and button
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
