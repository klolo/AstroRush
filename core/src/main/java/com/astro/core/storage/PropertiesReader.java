package com.astro.core.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Reader properties file from core and from game implementation and keep it in memory. This is singleton and initialization is on the first
 * usage.
 */
@Slf4j
public enum PropertiesReader {
    instance;

    /**
     * Name of properties file.
     */
    private final static String CORE_PROPERTIES_NAME = "core.properties";

    /**
     * Name of properties file in game implementation.
     */
    private final static String GAME_PROPERTIES_NAME = "game.properties";

    /**
     * Read properties file.
     */
    @Getter
    private static Properties gameProperties = null;

    public void init() {
        gameProperties = new Properties();
        readPropertiesFile(CORE_PROPERTIES_NAME);
        readPropertiesFile(GAME_PROPERTIES_NAME);
    }

    /**
     * Open stream and read poroperties file from classpath.
     *
     * @param fileName - name of the properties file.
     */
    private void readPropertiesFile(String fileName) {
        log.info("Reading properties file: {}", fileName);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(fileName);

        if (stream == null) {
            log.warn("File not found: {}", fileName);
        }

        try {
            Properties properties = new Properties();
            properties.load(stream);
            stream.close();

            gameProperties.putAll(properties);
            log.info("Read content: {}", properties);
        }
        catch (IOException e) {
            log.error("Error while reading game properties", e);
        }
    }

    /**
     * Getting property from game.properties.
     *
     * @return property value or empty string.
     */
    public String getProperty(String key) {
        if (gameProperties == null) {
            init();
            return gameProperties.getProperty(key);
        }
        return gameProperties.getProperty(key);
    }
}
