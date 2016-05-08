package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.SimpleImageVOToIGameObjectConverter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Class read and convert sImages array from json file.
 */
@Slf4j
public class ComponentLoader extends BaseLoader implements ILoader<SimpleImageVO> {

    /**
     *
     */
    public ComponentLoader() {
        log.info("..:: ComponentLoader ::..");
        resourceManager.initAllResources();
    }

    /**
     * @param imageVO
     */
    public IGameObject register(SimpleImageVO imageVO) {
        log.info("[register component]. ImageName: {}, Identifier: {}, Position: {} {}, Origin: {} {}, Layer: {}",
                imageVO.imageName, imageVO.itemIdentifier, imageVO.x, imageVO.y, imageVO.originX, imageVO.originY, imageVO.layerName);
        TextureRegion textureRegion = resourceManager.getTextureRegion(imageVO.imageName);
        float w = textureRegion.getRegionWidth();
        float h = textureRegion.getRegionHeight();

        IGameObject result;

        if (imageVO.physics != null) {
            try {
                result = new PhysicsObject(textureRegion);
                Body physicsBody = createBoody(imageVO, w, h, imageVO.imageName);
                physicsBody.setUserData(result);
                ((PhysicsObject) result).setBody(physicsBody);
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
        result.getSprite().setBounds(
                imageVO.x,
                imageVO.y,
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight()
        );

        return result;
    }


}
