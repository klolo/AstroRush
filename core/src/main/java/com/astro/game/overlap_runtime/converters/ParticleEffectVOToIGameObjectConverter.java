package com.astro.game.overlap_runtime.converters;

import com.astro.game.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import org.springframework.stereotype.Component;

/**
 * Rewrite SimpleImageVO data to IGameObject.
 */
@Component
public class ParticleEffectVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<ParticleEffectVO> {

    @Override
    public IGameObject convert(ParticleEffectVO particleEffectVO, IGameObject result) {
        super.convert(particleEffectVO,result);

        result.getData().getSprite().setBounds(
                particleEffectVO.x,
                particleEffectVO.y,
                particleEffectVO.particleWidth,
                particleEffectVO.particleHeight
        );

        return result;
    }
}
