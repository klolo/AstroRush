package com.astro.desktop;

import com.astro.core.engine.base.GameEngine;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.base.Preconditions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class of the AstroRush 2.0 game.
 * It creates game engine instance and run libgdx window.
 */
@Slf4j
public class Launcher {

    /**
     * Main loop of the game.
     */
    @Autowired
    @Setter
    private GameEngine gameEngine;

    @Setter
    @Value("${window.fullscreen}")
    private boolean fullscreen;

    @Setter
    @Value("${window.width}")
    private int windowWidth;

    @Setter
    @Value("${window.height}")
    private int windowHeight;

    @Setter
    @Value("${window.title}")
    private String windowTitle;

    @Setter
    @Value("${window.faviconPath}")
    private String faviconPath;

    private final static int ERROR_EXIT_CODE = -1;

    @Setter
    @Value("${game.version}")
    private String applicationVersion;

    /**
     * Main method of the application.
     */
    public static void main(final String... args) {
        final ApplicationContext context =
                new ClassPathXmlApplicationContext("desktop-context.xml");
        final Launcher launcher = context.getBean(Launcher.class);
        launcher.run();
    }

    /**
     * Create libgdx app context and put inside configuration.
     */
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})
    private void run() {
        LOGGER.info("AstroRush 2.0 starting (version: {})", applicationVersion);
        Preconditions.checkNotNull(gameEngine, "GameEngine is not initialized");

        try {
            new LwjglApplication(gameEngine, getConfig());
        }
        catch (final Exception e) {
            LOGGER.error("Fatal error in game", e);
            System.exit(ERROR_EXIT_CODE);
        }
    }

    /**
     * Preparing application configuration.
     *
     * @return config for LwjglApplication.
     */
    private LwjglApplicationConfiguration getConfig() {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = windowWidth;
        config.height = windowHeight;
        config.fullscreen = fullscreen;
        config.vSyncEnabled = true;
        config.title = windowTitle;
        config.addIcon(faviconPath, Files.FileType.Classpath);
        return config;
    }

}
