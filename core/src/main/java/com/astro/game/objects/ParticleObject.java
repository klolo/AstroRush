package com.astro.game.objects;

import com.astro.game.adnotation.Dispose;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class ParticleObject extends TextureObject {

    @Setter
    @Dispose
    protected ParticleEffect effect;

    @Override
    protected void render(final OrthographicCamera cam, final float delta) {
        effect.setPosition(
                data.sprite.getX() * pixelPerMeter,
                data.sprite.getY() * pixelPerMeter
        );
        effect.draw(batch, delta);
    }

}
