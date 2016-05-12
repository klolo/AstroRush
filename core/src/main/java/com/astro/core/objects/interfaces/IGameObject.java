package com.astro.core.objects.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Represents a game object interface.
 */
public interface IGameObject {

    /**
     * Name field in json file, which point to logic implementation class for current object.
     */
    String LOGIC_VARS = "logic";

    void show(OrthographicCamera cam, float delta);

    Sprite getSprite();

    void setName(String name);

    void setLayerID(String layerName);

    String getLayerID();

    boolean hasLogic();

    ILogic getLogic();

    void setCustomVar(String key, String val);

    void setLogic(ILogic logic);

    void update(float delta);

    boolean isPhysicObject();

    String getName();

    Body getBody();

    void setBody(Body body);

    void setItemIdentifier(final String id);

    String getItemIdentifier();

    boolean isDestroyed();

    void setIsDestroyed(boolean flag);
}
