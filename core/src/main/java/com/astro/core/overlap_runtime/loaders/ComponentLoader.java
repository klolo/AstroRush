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
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Class read and convert sImages array from json file.
 */
@Slf4j
public class ComponentLoader implements ILoader<SimpleImageVO> {

    /**
     * Pixel per meter.
     */
    private int PPM = 0;

    /**
     *
     */
    public ComponentLoader() {
        log.info("..:: ComponentLoader ::..");
        resourceManager.initAllResources();
        PPM = PhysicsWorld.instance.PIXEL_PER_METER;
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
                LinkedList<PolygonShape> polygons = new LinkedList<>();

                Arrays.stream(imageVO.shape.polygons)
                        .forEach(
                                vec ->
                                        polygons.add(
                                                getPolygonShape(vec, imageVO, w, h)
                                        ));

                Body body = PhysicsWorld.instance.createBody(getBodyDef(imageVO), imageVO.imageName);
                polygons.forEach(e -> body.createFixture(getFixtureDefinition(e, imageVO)));
                ((PhysicsObject) result).setBody(body);
                body.setUserData(result);
            }
            catch(final Exception e) {
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

    /**
     *
     */
    private BodyDef getBodyDef(SimpleImageVO imageVO) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(imageVO.x, imageVO.y);
        bodyDef.angle = (float) Math.toRadians(imageVO.rotation);

        int bodyType = imageVO.physics.bodyType;

        if (bodyType == 0) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        else if (bodyType == 1) {
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        }
        else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        return bodyDef;
    }

    /**
     *
     */
    private PolygonShape getPolygonShape(Vector2[] vertices, SimpleImageVO image, float w, float h) {
        PolygonShape shape = new PolygonShape();
        for (Vector2 it : vertices) {
            it.x = ((it.x + image.originX) - (w / PPM / 2)) * image.scaleX;
            it.y = ((it.y + image.originY) - (h / PPM / 2)) * image.scaleY;
        }

        shape.set(vertices);
        return shape;
    }

    /**
     *
     */
    protected FixtureDef getFixtureDefinition(PolygonShape shape, SimpleImageVO imageVO) {
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = imageVO.physics.density;
        fixtureDef.friction = imageVO.physics.friction;
        fixtureDef.restitution = 0.3f;//imageVO.physics.restitution; // Make it bounce a little bit

        return fixtureDef;
    }
}
