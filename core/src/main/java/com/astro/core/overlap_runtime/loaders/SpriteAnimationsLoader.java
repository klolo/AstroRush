package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.MainItemVOToIGameObjectConverter;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SpriteAnimationsLoader extends BaseLoader implements ILoader<SpriteAnimationVO>, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Override
    public IGameObject register(final SpriteAnimationVO spriteAnimationVO) {
        final TextureAtlas atlas = GameResources.instance.getResourceManager().getSpriteAnimation(spriteAnimationVO.animationName);
        final AnimationObject result = applicationContext.getBean(AnimationObject.class);
        result.initAnimation(spriteAnimationVO.fps, atlas);

        initPhysics(spriteAnimationVO, atlas, result);

        result.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
        result.getAnimation().setFrameDuration(0.1f);
        result.setPhysicsEngine(physicsEngine);

        return new MainItemVOToIGameObjectConverter().convert(spriteAnimationVO, result);
    }

    private void initPhysics(final SpriteAnimationVO spriteAnimationVO, final TextureAtlas atlas, final AnimationObject result) {
        if (spriteAnimationVO.physics != null) {
            final float w = atlas.getRegions().get(0).getRegionWidth();
            final float h = atlas.getRegions().get(0).getRegionHeight();
            final Body physicsBody = createBody(spriteAnimationVO, w, h, spriteAnimationVO.animationName);
            physicsBody.setUserData(result);
            result.getData().setBody(physicsBody);
        }
    }
}
