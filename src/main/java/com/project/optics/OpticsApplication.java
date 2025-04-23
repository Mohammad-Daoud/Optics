package com.project.optics;

import com.project.optics.utils.NetworkUtil;
import com.project.optics.utils.SwaggerUtils;
import com.project.optics.utils.SwingUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class OpticsApplication {

	public static void main(String[] args) {
		SwingUtils.showLoadingMessage();
		ConfigurableApplicationContext context = SpringApplication.run(OpticsApplication.class, args);
		NetworkUtil.openBrowser(NetworkUtil.getUrl(context));
		SwingUtils.openUIWindow(context);
		SwingUtils.closeLoadingMessage();
	}
}
