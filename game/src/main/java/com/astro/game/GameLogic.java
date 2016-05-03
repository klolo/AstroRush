package com.astro.game;

import com.astro.core.engine.GameScreen;
import com.astro.core.engine.IGameLogic;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents implementation of the Astro Rush 2.0 game.
 */
@Slf4j
public class GameLogic implements IGameLogic {

    @Getter
    private GameScreen gameScreen;

    private Player player;

    public void init() {
        log.info("AstroGame init");
        gameScreen = new GameScreen("scenes/MainScene.dt");
        KeyObserve.instance.register(this);
        this.player = new Player();
        gameScreen.setPlayer(player);
    }

    public void render(float deltaTime) {
        if (gameScreen != null) {
            gameScreen.render(deltaTime);
        }
    }

    public void update(float time) {
        gameScreen.update(time);
    }

    /**
     * Called on the game end, used for cleaning resources etc.
     */
    public void onExit() {
        log.info("cleaning resources before end");
    }

    @Override
    public void keyPressEvent(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }
}
