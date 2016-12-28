package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.ParticleObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.ParticleEffectVOToIGameObjectConverter;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParticleEffectsLoader implements ILoader<ParticleEffectVO> {

    @Autowired
    private GameResources gameResources;
    
    /**
     * Density of the ground box.
     */
    @Setter
    @Value("${renderer.pixel.per.meter}")
    protected short pixelPerMeter;

    @Override
    public IGameObject register(final ParticleEffectVO particleEffectVO) {
        final ParticleObject result = new ParticleObject();
        final ParticleEffect effect = gameResources.getResourceManager().getParticleEffect(particleEffectVO.particleName);

        final float pX = particleEffectVO.x * pixelPerMeter - particleEffectVO.particleWidth * particleEffectVO.scaleX / 2;
        final float pY = particleEffectVO.y * pixelPerMeter - particleEffectVO.particleHeight * particleEffectVO.scaleY / 2;

        effect.setPosition(pX, pY);
        result.setEffect(effect);

        return new ParticleEffectVOToIGameObjectConverter().convert(particleEffectVO, result);
    }

}
