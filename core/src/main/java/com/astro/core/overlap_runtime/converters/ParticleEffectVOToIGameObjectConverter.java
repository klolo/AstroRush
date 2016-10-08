package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import org.springframework.stereotype.Component;

/**
 * Rewrite SimpleImageVO data to IGameObject.
 */
@Component
public class ParticleEffectVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<ParticleEffectVO> {

    @Override
    public IGameObject convert(final ParticleEffectVO particleEffectVO, final IGameObject result) {
        super.convert(particleEffectVO, result);

        result.getData().getSprite().setBounds(
                particleEffectVO.x,
                particleEffectVO.y,
                particleEffectVO.particleWidth,
                particleEffectVO.particleHeight
        );

        return result;
    }
}
