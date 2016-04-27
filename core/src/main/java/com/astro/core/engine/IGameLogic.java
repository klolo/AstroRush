package com.astro.core.engine;

import com.astro.core.observe.IKeyObserver;
import com.badlogic.gdx.Screen;

/**
 * Represents game logic.
 */
public interface IGameLogic extends IKeyObserver {

    void render(float deltaTime);

    void update(float time);

    void init();

    Screen getGameScreen();

    void onExit();
}

