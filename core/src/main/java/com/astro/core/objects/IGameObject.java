package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by kamil on 26.04.16.
 */
public interface IGameObject {

    void render(OrthographicCamera cam);

    Sprite getSprite();

    void setName(String name);
}
