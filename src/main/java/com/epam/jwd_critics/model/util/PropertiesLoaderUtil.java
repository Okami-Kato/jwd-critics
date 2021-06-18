package com.epam.jwd_critics.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoaderUtil {

    private final Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtil.class);

    private final Properties properties = new Properties();

    private ApplicationProperties appProperties;

    private PropertiesLoaderUtil() {
        loadProperties();
    }

    public static ApplicationProperties getApplicationProperties() {
        return PropertiesLoaderUtilSingleton.INSTANCE.appProperties;
    }

    private void loadProperties() {
        try (InputStream input = PropertiesLoaderUtil.class.getClassLoader().getResourceAsStream("properties/application.properties")) {
            properties.load(input);
            appProperties = new ApplicationProperties(
                    properties.getProperty("url"),
                    properties.getProperty("databaseName"),
                    properties.getProperty("user"),
                    properties.getProperty("password"),
                    Integer.parseInt(properties.getProperty("minPoolSize")),
                    Integer.parseInt(properties.getProperty("maxPoolSize"))
            );
            logger.info("Application properties loaded");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static class PropertiesLoaderUtilSingleton {
        private static final PropertiesLoaderUtil INSTANCE = new PropertiesLoaderUtil();
    }
}
