package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.uwsoft.editor.renderer.scripts.IScript;

/**
 * Created by kamil on 23.04.16.
 */
public interface IPlayer extends IScript{

    float getPositionX();

    float getPositionY();

    void dispose();

    void render(OrthographicCamera cam);

    void update(float diff);

}
