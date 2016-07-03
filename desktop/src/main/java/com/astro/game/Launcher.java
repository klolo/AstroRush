package com.astro.game;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.interfaces.IGameLogic;
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
 * It creates game engine instance and run libgdx window.
 */
@Slf4j
public class Launcher {

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

    @Setter
    private boolean fullscreen;

    @Setter
    private int windowWidth;

    @Setter
    private int windowHeight;

    @Setter
    private String windowTitle;

    @Setter
    private String faviconPath;

    private final static int ERROR_EXIT_CODE = -1;

    /**
     * Main method of the application.
     */
    public static void main(String... args) {
        final ApplicationContext context =
                new ClassPathXmlApplicationContext("desktop.xml");
        GameEngine.setApplicationContext(context);

        final Launcher launcher = context.getBean(Launcher.class);
        launcher.run();
    }

    /**
     * Create libgdx app context and put inside configuration.
     */
    private void run() {
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

        if (fullscreen) {
            config.fullscreen = true;
        }
        else {
            config.width = windowWidth;
            config.height = windowHeight;
        }

        config.vSyncEnabled = true;
        config.addIcon(faviconPath, Files.FileType.Classpath);
        config.title = windowTitle;
        return config;
    }

}
