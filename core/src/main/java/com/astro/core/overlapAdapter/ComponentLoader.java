package com.astro.core.overlapAdapter;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.PhysicsObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

import java.util.Arrays;
import java.util.LinkedList;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class read and convert sImages array from json file.
 */
@Slf4j
public class ComponentLoader implements ILoader<SimpleImageVO> {

    /**
     * Pixel per meter.
     */
    private int PPM;

    /**
     * Offset in composites.
     */
    @Setter
    private float parentX = 0.0f;

    /**
     * Offset in composites.
     */
    @Setter
    private float parentY = 0.0f;

    /**
     * parent rotate composites.
     */
    @Setter
    private float parentRotate = 0.0f;

    /**
     *
     */
    public ComponentLoader() {
        resourceManager.initAllResources();
        PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
    }

    /**
     *
     * @param imageVO
     */
    public IGameObject register(SimpleImageVO imageVO) {
        log.info("[register component]. ImageName: {}, Identifier: {}, Position: {} {}, Origin: {} {}",
                imageVO.imageName, imageVO.itemIdentifier, imageVO.x, imageVO.y, imageVO.originX, imageVO.originY);
        TextureRegion textureRegion = resourceManager.getTextureRegion(imageVO.imageName);

        float w = textureRegion.getRegionWidth();
        float h = textureRegion.getRegionHeight();
        IGameObject result;

        if (imageVO.physics != null) {
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
        }
        else {
            result = new TextureObject(textureRegion);
        }

        result.setName(imageVO.imageName);
        result.getSprite().setBounds(
                imageVO.x + parentX,
                imageVO.y + parentY,
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight()
        );
        result.getSprite().setOrigin(
                imageVO.originX,
                imageVO.originY
        );
        result.getSprite().setScale(
                imageVO.scaleX,
                imageVO.scaleY
        );

        result.getSprite().setColor(imageVO.tint[0], imageVO.tint[1], imageVO.tint[2], imageVO.tint[3]);
        result.getSprite().setRotation(imageVO.rotation + parentRotate);
        return result;
    }


    /**
     *
     */
    private BodyDef getBodyDef(SimpleImageVO imageVO) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(imageVO.x + parentX, imageVO.y + parentY);
        bodyDef.angle = (float) Math.toRadians(imageVO.rotation + parentRotate);

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
            it.x = (it.x + parentX - (w / PPM / 2)) * image.scaleX;
            it.y = (it.y + parentY - (h / PPM / 2)) * image.scaleY;
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
        // fixtureDef.restitution = imageVO.physics.restitution; // Make it bounce a little bit

        return fixtureDef;
    }


}
