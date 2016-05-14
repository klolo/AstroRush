package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.base.GameResources;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.SimpleImageVOToIGameObjectConverter;
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
     * @param imageVO
     */
    public IGameObject register(SimpleImageVO imageVO) {
        log.info("[register component]. ImageName: {}, Identifier: {}, Position: {} {}, Origin: {} {}, Layer: {}",
                imageVO.imageName, imageVO.itemIdentifier, imageVO.x, imageVO.y, imageVO.originX, imageVO.originY, imageVO.layerName);
        TextureRegion textureRegion = GameResources.instance.getResourceManager().getTextureRegion(imageVO.imageName);
        float w = textureRegion.getRegionWidth();
        float h = textureRegion.getRegionHeight();

        IGameObject result;

        if (imageVO.physics != null) {
            try {
                result = new PhysicsObject(textureRegion);
                Body physicsBody = createBoody(imageVO, w, h, imageVO.imageName);
                physicsBody.setUserData(result);
                result.getData().setBody(physicsBody);
            }
            catch (final Exception e) {
                log.error("Incorrect physics settings");
                result = new TextureObject(textureRegion);
            }
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
