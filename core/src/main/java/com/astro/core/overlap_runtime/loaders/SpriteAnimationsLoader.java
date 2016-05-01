package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.MainItemVOToIGameObjectConverter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

/**
 * Created by kamil on 01.05.16.
 */
public class SpriteAnimationsLoader implements ILoader<SpriteAnimationVO> {

    public SpriteAnimationsLoader() {
        resourceManager.initAllResources();
    }

    @Override
    public IGameObject register(SpriteAnimationVO spriteAnimationVO) {
        AnimationObject result = new AnimationObject(
                spriteAnimationVO.fps,
                resourceManager.getSpriteAnimation(spriteAnimationVO.animationName)
        );

        result.getAnimation().setPlayMode( Animation.PlayMode.NORMAL );
        result.getAnimation().setFrameDuration( 0.1f );
        return new MainItemVOToIGameObjectConverter().convert(spriteAnimationVO,result);
    }
}
