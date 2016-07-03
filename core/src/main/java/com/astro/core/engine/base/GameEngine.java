package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.google.common.base.Preconditions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main loop of the game.
 */
@Slf4j
public class GameEngine extends Game {

    @Setter
    private float timeStep = 0;

    /**
     * Implemented game logic.
     */
    private IGameLogic gameLogic;

    /**
     * Used to make fixed time loop.
     */
    private float accumulator = 0;

    /**
     * Current GameStage.
     */
    private Screen screen;

    @Setter
    @Autowired
    private PhysicsEngine physicsEngine;

    /**
     * Requires game logic object, which will be updated and rendered.
     */
    public GameEngine(final IGameLogic gameLogic) {
        Preconditions.checkNotNull(gameLogic, "GameLogic cannot be null");
        this.gameLogic = gameLogic;
    }

    /**
     * Call implemented game init and set in super Screen field.
     */
    @Override
    public void create() {
        log.info("create");
        physicsEngine.initPhysics();

        gameLogic.init();
        screen = gameLogic.getGameScreen();

        Preconditions.checkNotNull(screen, "Screen cannot b null");
        setScreen(screen);
    }

    /**
     * Base od super.render(), but has additional update gameLogic. Render method is using for game update:
     *
     * @see <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">The-life-cycle</a>
     */
    @Override
    public void render() {
        if (gameLogic.getGameScreen() != screen) {
            setScreen(gameLogic.getGameScreen());
        }

        KeyObserve.instance.handleInput();

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
