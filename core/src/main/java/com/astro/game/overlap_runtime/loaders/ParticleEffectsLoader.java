package com.astro.game.overlap_runtime.loaders;

import com.astro.game.objects.ParticleObject;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.overlap_runtime.converters.ParticleEffectVOToIGameObjectConverter;
import com.astro.game.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParticleEffectsLoader implements ILoader<ParticleEffectVO> {

    /**
     * Density of the ground box.
     */
    @Setter
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter = 0;

    @Override
    public IGameObject register(final ParticleEffectVO particleEffectVO) {
        final ParticleObject result = new ParticleObject();
        final ParticleEffect effect = GameResources.instance.getResourceManager().getParticleEffect(particleEffectVO.particleName);

        float pX = particleEffectVO.x * pixelPerMeter - particleEffectVO.particleWidth * particleEffectVO.scaleX / 2;
        float pY = particleEffectVO.y * pixelPerMeter - particleEffectVO.particleHeight * particleEffectVO.scaleY / 2;

        effect.setPosition(pX, pY);
        result.setEffect(effect);

        return new ParticleEffectVOToIGameObjectConverter().convert(particleEffectVO,result);
    }

}
