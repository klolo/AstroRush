package com.astro.game.overlap_runtime.loaders;

import com.astro.game.objects.PhysicsObject;
import com.astro.game.objects.TextureObject;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.overlap_runtime.converters.SimpleImageVOToIGameObjectConverter;
import com.astro.game.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Class read and convert sImages array from json file.
 */
@Slf4j
@Component
public class ComponentLoader extends BaseLoader implements ILoader<SimpleImageVO>, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    /**
     * Register simple image.
     */
    public IGameObject register(final SimpleImageVO imageVO) {
        final TextureRegion textureRegion = GameResources.instance.getResourceManager().getTextureRegion(imageVO.imageName);
        final float w = textureRegion.getRegionWidth();
        final float h = textureRegion.getRegionHeight();

        IGameObject result;

        if (imageVO.physics != null) {
            result = applicationContext.getBean(PhysicsObject.class);


            final Body physicsBody = createBody(imageVO, w, h, imageVO.imageName);
            physicsBody.setUserData(result);
            result.getData().setBody(physicsBody);
        }
        else {
            result = applicationContext.getBean("textureObject", TextureObject.class);
        }

        ((TextureObject) result).setTextureRegion(textureRegion);
        result = new SimpleImageVOToIGameObjectConverter().convert(imageVO, result);
        result.getData().getSprite().setBounds(
                imageVO.x,
                imageVO.y,
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight()
        );

        return result;
    }


}
