package com.astro.core.engine.interfaces;

import com.badlogic.gdx.Screen;

/**
 * Represents game logic.
 */
public interface IGameLogic {

    void render(float deltaTime);

    void update(float time);

    void init();

    Screen getGameScreen();

    void onExit();
}

