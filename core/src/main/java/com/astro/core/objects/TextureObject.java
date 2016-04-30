package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by kamil on 30.04.16.
 */
public class TextureObject extends GameObject {

    /**
     * Requires texture for rendering.
     */
    public TextureObject(TextureRegion region) {
        super(region);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(OrthographicCamera cam, float delta) {
        super.draw();
    }

}
