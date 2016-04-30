package com.astro.core.objects.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by kamil on 26.04.16.
 */
public interface IGameObject {

    void show(OrthographicCamera cam, float delta);

    Sprite getSprite();

    void setName(String name);
}
