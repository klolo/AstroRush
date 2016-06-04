package com.astro.core.objects.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Represents a logic of the game object.
 */
public interface ILogic {

    void update(float diff);

    /**
     * Called after render game object with have this logic.
     */
    void additionalRender(final OrthographicCamera cam, float delta);

    void setGameObject(final IGameObject gameObject);

    void onPause();

    void onResume();

}
