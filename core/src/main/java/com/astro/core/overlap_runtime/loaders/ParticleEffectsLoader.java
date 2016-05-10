package com.astro.core.overlap_runtime.loaders;

import com.astro.core.GameResources;
import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.ParticleObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.ParticleEffectVOToIGameObjectConverter;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleEffectsLoader implements ILoader<ParticleEffectVO> {

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    public ParticleEffectsLoader() {
        log.info("..:: LightsLoader ::..");
        PropertyInjector.instance.inject(this);
    }

    @Override
    public IGameObject register(ParticleEffectVO particleEffectVO) {
        log.info("[register particle] name: {}, particleName: {} ", particleEffectVO.itemName, particleEffectVO.particleName);
        ParticleObject result = new ParticleObject();

        ParticleEffect effect = GameResources.instance.getResourceManager().getParticleEffect(particleEffectVO.particleName);

        float pX = particleEffectVO.x * PIXEL_PER_METER - particleEffectVO.particleWidth * particleEffectVO.scaleX / 2;
        float pY = particleEffectVO.y * PIXEL_PER_METER - particleEffectVO.particleHeight * particleEffectVO.scaleY / 2;

        effect.setPosition(pX, pY);
        result.setEffect(effect);

        return new ParticleEffectVOToIGameObjectConverter().convert(particleEffectVO,result);
    }

}
