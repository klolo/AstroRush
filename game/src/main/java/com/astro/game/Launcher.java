package com.astro.game;

import com.astro.core.engine.GameEngine;
import com.astro.core.storage.PropertiesReader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class of the AstroRush 2.0 game.
 */
@Slf4j
public class Launcher {

    public static void main(String... args) {
        log.info("Game start");
        GameLogic gameLogic = null;

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
            Optional.of(gameLogic).ifPresent(GameLogic::onExit);
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

        config.vSyncEnabled = false;
        config.addIcon("assets/ico.png", Files.FileType.Classpath);
        config.title = PropertiesReader.instance.getProperty("game.title");

        return config;
    }

}
