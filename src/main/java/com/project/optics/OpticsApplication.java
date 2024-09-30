package com.project.optics;




import com.project.optics.utils.NetworkUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;


@SpringBootApplication
public class OpticsApplication {


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(OpticsApplication.class, args);
		String url = getUrl(context);
		openBrowser(url); // Auto-open browser after startup
	}

	private static String getUrl(ApplicationContext context) {
		Environment environment = context.getBean(Environment.class);
		String port = environment.getProperty("server.port");
		String localIP = NetworkUtil.getLocalIpAddress();

		return "http://"+ localIP+":" + port;
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
			// ignore
		}
	}
}
