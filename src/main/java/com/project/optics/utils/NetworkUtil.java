package com.project.optics.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Filters out loopback and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Return the first non-loopback IPv4 address
                    if (addr.isSiteLocalAddress() && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "localhost";
    }

    public static String getUrl(ApplicationContext context) {
        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("server.port");
        String localIP = NetworkUtil.getLocalIpAddress();

        return "http://" + localIP + ":" + port;
    }

    public static void openBrowser(ApplicationContext context) {
       String url = getUrl(context);
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
}