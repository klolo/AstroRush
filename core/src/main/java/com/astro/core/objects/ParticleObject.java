package com.astro.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleObject extends GameObject {

    @Setter
    protected ParticleEffect effect;

    public ParticleObject() {
        super();
    }

    @Override
    public void render(OrthographicCamera cam, float delta) {
        Vector3 projectionPosition = cam.project(new Vector3(getSprite().getX(), getSprite().getY(), 0.0f));
        effect.setPosition(
                projectionPosition.x,
                projectionPosition.y
        );

        batch.begin();
        effect.draw(batch, delta);
        batch.end();
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    @Override
    public void setName(String name) {

    }
}
