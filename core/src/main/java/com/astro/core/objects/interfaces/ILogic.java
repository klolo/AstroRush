package com.astro.core.objects.interfaces;

import com.astro.core.objects.GameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.LinkedList;
import java.util.List;

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

    List<GameObject> children = new LinkedList<>();

    default List<GameObject> getChildren() {
        return children;
    }
}
