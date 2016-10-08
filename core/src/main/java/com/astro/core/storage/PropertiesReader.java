package com.astro.core.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reader properties file from core and from game implementation and keep it in memory. This is singleton and initialization is on the first
 * usage.
 */
@Slf4j
public enum PropertiesReader {
    instance;

    /**
     * Holds all international messages.
     */
    private MessagesManager messages = new MessagesManager();

    /**
     * Name of properties file in game implementation.
     */
    private final static String GAME_PROPERTIES_NAME = "application.properties";

    /**
     * Name of properties file in game implementation.
     */
    private final static String LANGUAGES_FILE_PREFIX = "properties/msg_";

    /**
     * Read properties file.
     */
    @Getter
    private static Properties gameProperties;

    public void init() {
        gameProperties = new Properties();
        gameProperties.putAll(readPropertiesFile(GAME_PROPERTIES_NAME));

        messages.setSelectedLanguages(gameProperties.getProperty("game.lang.default"));
        final String languages[] = gameProperties.getProperty("game.lang.available").trim().split(",");

        for (final String lng : languages) {
            messages.addLanguages(lng, readPropertiesFile(LANGUAGES_FILE_PREFIX + lng + ".properties"));
        }
    }

    /**
     * Open stream and read poroperties file from classpath.
     *
     * @param fileName - name of the properties file.
     */
    public Properties readPropertiesFile(final String fileName) {
        LOGGER.info("Reading properties file: {}", fileName);

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = loader.getResourceAsStream(fileName);

        if (stream == null) {
            LOGGER.warn("File not found: {}", fileName);
        }

        try {
            final Properties properties = new Properties();
            properties.load(stream);
            stream.close();
            LOGGER.info("Read content: {}", properties);
            return properties;
        }
        catch (IOException e) {
            LOGGER.error("Error while reading game properties", e);
        }

        return null;
    }

    /**
     * Getting property from game.properties.
     *
     * @return property value or empty string.
     */
    public String getProperty(final String key) {
        if (gameProperties == null) {
            init();
            return gameProperties.getProperty(key);
        }
        return gameProperties.getProperty(key);
    }

    public String getMsg(final String key) {
        if (messages == null) {
            init();
            return messages.getMsg(key);
        }
        return messages.getMsg(key);
    }

}
