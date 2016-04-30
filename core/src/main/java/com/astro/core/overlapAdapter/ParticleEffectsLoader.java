package com.astro.core.overlapAdapter;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.ParticleObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class ParticleEffectsLoader implements ILoader<ParticleEffectVO> {

    /**
     * It will load effects by name.
     */
    private ResourceManager resourceManager = new ResourceManager();

    public ParticleEffectsLoader() {
        resourceManager.initAllResources();
    }


    @Override
    public IGameObject register(ParticleEffectVO particleEffectVO) {
        log.info("[register particle] name: {}, particleName: {} ", particleEffectVO.itemName, particleEffectVO.particleName);
        ParticleObject result = new ParticleObject();
        result.setName(particleEffectVO.particleName);

        result.getSprite().setBounds(
                particleEffectVO.x,
                particleEffectVO.y,
                particleEffectVO.particleWidth,
                particleEffectVO.particleHeight
        );
        result.getSprite().setOrigin(
                particleEffectVO.originX,
                particleEffectVO.originY
        );
        result.getSprite().setScale(
                particleEffectVO.scaleX,
                particleEffectVO.scaleY
        );

        result.getSprite().rotate(particleEffectVO.rotation);
        ParticleEffect effect = resourceManager.getParticleEffect(particleEffectVO.particleName);
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();

        float pX = particleEffectVO.x * PPM - (particleEffectVO.particleWidth * particleEffectVO.scaleX / 2);
        float pY = particleEffectVO.y * PPM - (particleEffectVO.particleHeight * particleEffectVO.scaleY / 2);

        effect.setPosition(pX, pY);
        result.setEffect(effect);
        return result;
    }

}
