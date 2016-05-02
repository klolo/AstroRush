package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
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
        effect.setPosition(
                sprite.getX() * PhysicsWorld.instance.getPIXEL_PER_METER(),
                sprite.getY() * PhysicsWorld.instance.getPIXEL_PER_METER()
        );
        effect.draw(batch, delta);
    }

}
