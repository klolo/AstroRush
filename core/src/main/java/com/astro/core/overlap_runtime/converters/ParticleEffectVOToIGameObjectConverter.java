package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;

/**
 * Rewrite SimpleImageVO data to IGameObject.
 */
public class ParticleEffectVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<ParticleEffectVO> {

    @Override
    public IGameObject convert(ParticleEffectVO particleEffectVO, IGameObject result) {
        super.convert(particleEffectVO,result);

        result.getSprite().setBounds(
                particleEffectVO.x,
                particleEffectVO.y,
                particleEffectVO.particleWidth,
                particleEffectVO.particleHeight
        );

        return result;
    }
}
