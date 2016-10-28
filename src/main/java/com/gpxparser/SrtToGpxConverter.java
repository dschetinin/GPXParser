package com.gpxparser;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class SrtToGpxConverter {

    private static final Logger logger = LogManager.getLogger(SrtToGpxConverter.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SrtToGpxConverter.class, args);

        // validate log4j java VM option on input
        String log4jProperty = System.getProperty("log4j.configuration");
        if (log4jProperty == null || log4jProperty.length() <= 0) {
            System.out.println("log4j.configuration system property not found, using default one");
            Properties props = new Properties();
            try (InputStream in = SrtToGpxConverter.class.getClassLoader().getResourceAsStream("log4j.properties")) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("Can not load configuration file ");
                e.printStackTrace(System.err);
            }
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(props);
        }
    }
}
