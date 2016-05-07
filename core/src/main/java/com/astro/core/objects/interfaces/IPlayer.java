package com.astro.core.objects.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.scripts.IScript;

/**
 * Created by kamil on 23.04.16.
 */
public interface IPlayer {

    float getPositionX();

    float getPositionY();

    void dispose();

    void show(OrthographicCamera cam, float diff);

    Body getBody();

}
