package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.ParticleObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.ParticleEffectVOToIGameObjectConverter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleEffectsLoader implements ILoader<ParticleEffectVO> {

    public ParticleEffectsLoader() {
        log.info("..:: LightsLoader ::..");
        resourceManager.initAllResources();
    }

    @Override
    public IGameObject register(ParticleEffectVO particleEffectVO) {
        log.info("[register particle] name: {}, particleName: {} ", particleEffectVO.itemName, particleEffectVO.particleName);
        ParticleObject result = new ParticleObject();

        ParticleEffect effect = resourceManager.getParticleEffect(particleEffectVO.particleName);
        float PPM = PhysicsWorld.instance.PIXEL_PER_METER;

        float pX = particleEffectVO.x * PPM - (particleEffectVO.particleWidth * particleEffectVO.scaleX / 2);
        float pY = particleEffectVO.y * PPM - (particleEffectVO.particleHeight * particleEffectVO.scaleY / 2);

        effect.setPosition(pX, pY);
        result.setEffect(effect);

        return new ParticleEffectVOToIGameObjectConverter().convert(particleEffectVO,result);
    }

}
