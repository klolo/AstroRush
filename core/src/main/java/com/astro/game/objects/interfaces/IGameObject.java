package com.astro.game.objects.interfaces;

import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.objects.ObjectData;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Represents a game object interface.
 */
public interface IGameObject {

    /**
     * Name field in json file, which point to logic implementation class for current object.
     */
    String LOGIC_VARS = "logic";

    void show(final OrthographicCamera cam, final float delta);

    boolean hasLogic();

    void update(final float delta);

    ObjectData getData();

    boolean isPhysicObject();

    PhysicsEngine getPhysicsEngine();

    void setPhysicsEngine(final PhysicsEngine physicsEngine);

    boolean isRenderingInScript();
}
