package com.astro.core.engine.stage.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Game hud displayed on the level screen..
 */
public interface IGameHud {

    void show(final OrthographicCamera cam, float delta);

    void init();
}
