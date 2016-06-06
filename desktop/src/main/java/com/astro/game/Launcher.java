package com.astro.game;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.base.GameLogic;
import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.storage.PropertiesReader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Main class of the AstroRush 2.0 game.
 */
@Slf4j
public class Launcher {

    /**
     * Game logic implemented for this title.
     */
    private IGameLogic gameLogic = null;

    /**
     * Main method of the application.
     */
    public static void main(String... args) {
        Launcher launcher = new Launcher();
        launcher.run();
    }

    private void run() {
        log.info("Game start");

        try {
            Launcher launcher = new Launcher();
            gameLogic = new GameLogic();
            new LwjglApplication(new GameEngine(gameLogic), launcher.getConfig());
            log.info("Game end");
        }
        catch (final Exception e) {
            log.error("Fatal error in game", e);
            System.exit(-1);
        }
        finally {
            Optional.ofNullable(gameLogic).ifPresent(e -> gameLogic.onExit());
        }
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
