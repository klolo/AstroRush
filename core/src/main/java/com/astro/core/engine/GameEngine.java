package com.astro.core.engine;

import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lombok.extern.slf4j.Slf4j;

/**
 * Main loop of the game.
 */
@Slf4j
public class GameEngine extends Game {

    /**
     * Implemented game logic.
     */
    private IGameLogic gameLogic;

    /**
     * Box2d world.
     */
    private PhysicsWorld world;

    /**
     * Used to make fixed time loop.
     */
    private float accumulator = 0;

    /**
     * Requires game logic object, which will be updated and rendered.
     */
    public GameEngine(IGameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    /**
     * Call implemented game init and set in super Screen field.
     */
    @Override
    public void create() {
        log.info("create");
        gameLogic.init();
        setScreen(gameLogic.getGameScreen());
        world = PhysicsWorld.instance;
    }

    /**
     * Base od super.render(), but has additional update gameLogic. Render method is using for game update:
     *
     * @see <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">The-life-cycle</a>
     */
    @Override
    public void render() {
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
    private void update(float deltaTime) {
        gameLogic.update(deltaTime);

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        float step = world.getTIME_STEP();

        while (accumulator >= step) {
            PhysicsWorld.instance.step();
            accumulator -= step;
        }
    }
}
