package com.astro.core.objects.interfaces;

import com.astro.core.objects.GameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
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
    default void additionalRender(final OrthographicCamera cam, float delta) {

    }

    void setGameObject(final IGameObject gameObject);

    List<GameObject> children = new LinkedList<>();

    default List<GameObject> getChildren() {
        final List<GameObject> result = new ArrayList<>(children);
        children.clear();
        return result;
    }

    default void onResume() {

    }

    default void onPause() {

    }

}
