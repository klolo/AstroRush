package com.astro.core.engine;

import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author klolo
 */
@Slf4j
public class GameEngine extends Game {

    private OrthographicCamera camera;

    private IGameLogic gameLogic;

    private PhysicsWorld world;

    private float accumulator = 0;

    public GameEngine(IGameLogic gameLogic) {
        log.info("Engine init");
        this.gameLogic = gameLogic;
        camera = new OrthographicCamera();
    }

    /**
     * Call implemented game init and set in super Screen field.
     */
    @Override
    public void create() {
        gameLogic.init();
        setScreen(gameLogic.getGameScreen());
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
     *
     * @param deltaTime
     */
    private void update(float deltaTime) {
        gameLogic.update(deltaTime);

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        float step = PhysicsWorld.instance.getTIME_STEP();

        while (accumulator >= step) {
            PhysicsWorld.instance.step();
            accumulator -= step;
        }
    }

}
