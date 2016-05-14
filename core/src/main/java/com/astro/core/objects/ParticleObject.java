package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleObject extends TextureObject {

    @Setter
    @Dispose
    protected ParticleEffect effect;

    @Override
    protected void render(OrthographicCamera cam, float delta) {
        effect.setPosition(
                data.sprite.getX() * PIXEL_PER_METER,
                data.sprite.getY() * PIXEL_PER_METER
        );
        effect.draw(batch, delta);
    }

}
