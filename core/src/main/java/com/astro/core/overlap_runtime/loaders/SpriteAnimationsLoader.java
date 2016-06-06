package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.MainItemVOToIGameObjectConverter;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SpriteAnimationsLoader extends BaseLoader implements ILoader<SpriteAnimationVO> {

    @Override
    public IGameObject register(SpriteAnimationVO spriteAnimationVO) {
        final TextureAtlas atlas = GameResources.instance.getResourceManager().getSpriteAnimation(spriteAnimationVO.animationName);
        final AnimationObject result = new AnimationObject(spriteAnimationVO.fps, atlas);

        initPhysics(spriteAnimationVO, atlas, result);

        result.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
        result.getAnimation().setFrameDuration(0.1f);
        return new MainItemVOToIGameObjectConverter().convert(spriteAnimationVO, result);
    }

    private void initPhysics(final SpriteAnimationVO spriteAnimationVO, final TextureAtlas atlas, final AnimationObject result) {
        if (spriteAnimationVO.physics != null) {
            float w = atlas.getRegions().get(0).getRegionWidth();
            float h = atlas.getRegions().get(0).getRegionHeight();
            Body physicsBody = createBody(spriteAnimationVO, w, h, spriteAnimationVO.animationName);
            physicsBody.setUserData(result);
            result.getData().setBody(physicsBody);
        }
    }
}
