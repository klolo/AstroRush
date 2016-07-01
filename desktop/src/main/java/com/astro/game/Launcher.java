package com.astro.game;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.storage.PropertiesReader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.base.Preconditions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Optional;

/**
 * Main class of the AstroRush 2.0 game.
 */
@Slf4j
public class Launcher {

    private final static int ERROR_EXIT_CODE = -1;

    /**
     * Game logic implemented for this title.
     */
    @Autowired
    @Setter
    private IGameLogic gameLogic;

    /**
     * Main loop of the game.
     */
    @Autowired
    @Setter
    private GameEngine gameEngine;

    /**
     * Main method of the application.
     */
    public static void main(String... args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("application.xml");
        log.info("created spring context");

        Launcher launcher = context.getBean(Launcher.class);
        launcher.run();
    }

    /**
     * Create libgdx app context and put inside configuration.
     */
    private void run() {
        log.info("run");
        Preconditions.checkNotNull(gameEngine, "GameEngine is not initialized");

        try {
            new LwjglApplication(gameEngine, getConfig());
        }
        catch (final Exception e) {
            log.error("Fatal error in game", e);
            System.exit(ERROR_EXIT_CODE);
        }
        finally {
            Optional.ofNullable(gameLogic).ifPresent(e -> gameLogic.onExit());
        }

        log.info("Game end normally");
    }

    /**
     * Preparing application configuration.
     *
     * @return config for LwjglApplication.
     */
    private LwjglApplicationConfiguration getConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        if (Boolean.parseBoolean(PropertiesReader.instance.getProperty("window.fullscreen"))) {
            log.info("fullscreen mode");
            config.fullscreen = true;
        }
        else {
            log.info("windowed mode");
            config.width = Integer.valueOf(PropertiesReader.instance.getProperty("window.width"));
            config.height = Integer.valueOf(PropertiesReader.instance.getProperty("window.height"));
        }

        config.vSyncEnabled = true;
        config.addIcon("assets/ico.png", Files.FileType.Classpath);
        config.title = PropertiesReader.instance.getProperty("game.title");
        return config;
    }

}
