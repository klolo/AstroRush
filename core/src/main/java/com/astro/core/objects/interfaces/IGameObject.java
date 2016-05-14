package com.astro.core.objects.interfaces;

import com.astro.core.objects.ObjectData;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Represents a game object interface.
 */
public interface IGameObject {

    /**
     * Name field in json file, which point to logic implementation class for current object.
     */
    String LOGIC_VARS = "logic";

    void show(OrthographicCamera cam, float delta);

    boolean hasLogic();

    void update(float delta);

    boolean isPhysicObject();

    ObjectData getData();
}
