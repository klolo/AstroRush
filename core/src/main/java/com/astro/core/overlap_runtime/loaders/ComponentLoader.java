package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.SimpleImageVOToIGameObjectConverter;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.extern.slf4j.Slf4j;

/**
 * Class read and convert sImages array from json file.
 */
@Slf4j
public class ComponentLoader extends BaseLoader implements ILoader<SimpleImageVO> {

    /**
     * Register simple image.
     */
    public IGameObject register(final SimpleImageVO imageVO) {
        log.info("[register component]. ImageName: {}, Identifier: {}, Position: {} {}, Origin: {} {}, Layer: {}",
                imageVO.imageName, imageVO.itemIdentifier, imageVO.x, imageVO.y, imageVO.originX, imageVO.originY, imageVO.layerName);
        TextureRegion textureRegion = GameResources.instance.getResourceManager().getTextureRegion(imageVO.imageName);
        final float w = textureRegion.getRegionWidth();
        final float h = textureRegion.getRegionHeight();

        IGameObject result;

        if (imageVO.physics != null) {
            result = new PhysicsObject(textureRegion);
            Body physicsBody = createBody(imageVO, w, h, imageVO.imageName);
            physicsBody.setUserData(result);
            result.getData().setBody(physicsBody);
        }
        else {
            result = new TextureObject(textureRegion);
        }

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
