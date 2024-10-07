package com.project.optics;

import com.project.optics.utils.NetworkUtil;
import com.project.optics.utils.SwaggerUtils;
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
		NetworkUtil.openBrowser(context);
		SwaggerUtils.openUIWindow(context);
	}
}
