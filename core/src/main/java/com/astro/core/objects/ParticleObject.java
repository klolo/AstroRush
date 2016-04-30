package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleObject extends GameObject {

    @Setter
    protected ParticleEffect effect;

    @Override
    public void render(OrthographicCamera cam, float delta) {
        effect.draw(batch, delta);
    }

}
