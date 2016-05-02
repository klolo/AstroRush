package com.astro.core.objects.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Represents a game object interface.
 */
public interface IGameObject {

    void show(OrthographicCamera cam, float delta);

    Sprite getSprite();

    void setName(String name);

    void setLayerID(String layerName);

    String getLayerID();

    boolean hasLogic();
}
