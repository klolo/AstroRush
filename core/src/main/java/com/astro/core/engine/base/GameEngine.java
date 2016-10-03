package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

/**
 * Main loop of the game.
 */
@Slf4j
public class GameEngine extends Game {

    @Setter
    @Value("${world.time.step}")
    float timeStep = 0;

    /**
     * Implemented game logic.
     */
    private IGameLogic gameLogic;

    /**
     * Used to make fixed time loop.
     */
    private float accumulator = 0;

    private Screen screen;

    @Setter
    @Autowired
    PhysicsEngine physicsEngine;

    @Autowired
    private KeyObserve keyObserve;

    @Setter
    @Getter
    private static ApplicationContext applicationContext;

    /**
     * Requires game logic object, which will be updated and rendered.
     */
    @Autowired
    public GameEngine(final IGameLogic gameLogic) {
        Preconditions.checkNotNull(gameLogic, "GameLogic cannot be null");
        this.gameLogic = gameLogic;
    }

    /**
     * Call implemented game switchStage and set in super Screen field.
     */
    @Override
    public void create() {
        Preconditions.checkNotNull(physicsEngine, "Physics engine not injected");
        Preconditions.checkNotNull(applicationContext, "ApplicationContext not injected");
        Preconditions.checkNotNull(gameLogic, "Game logic cannot be null");

        log.info("creating game engine");

        physicsEngine.initPhysics();

        gameLogic.switchStage();
        screen = gameLogic.getCurrentScreen();

        Preconditions.checkNotNull(screen, "Screen cannot be null");
        setScreen(screen);
    }

    /**
     * Base od super.render(), but has additional update gameLogic. Render method is using for game update:
     *
     * @see <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">The-life-cycle</a>
     */
    @Override
    public void render() {
        if (gameLogic.getCurrentScreen() != screen) {
            setScreen(gameLogic.getCurrentScreen());
        }

        keyObserve.handleInput();

        float deltaTime = Gdx.graphics.getDeltaTime();
        update(deltaTime);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameLogic.render(deltaTime);
    }

    /**
     * Fixed step update game logic.
     */
    private void update(final float deltaTime) {
        gameLogic.update(deltaTime);

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        float step = 1 / timeStep;

        while (accumulator >= step) {
            physicsEngine.process();
            accumulator -= step;
        }
    }
}
