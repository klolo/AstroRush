package com.astro.core.overlapAdapter;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.IGameObject;
import com.astro.core.objects.PhysicsObject;
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

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 26.04.16.
 */
@Slf4j
public class ComponentLoader {


    int PPM;

    /**
     *
     */
    private ResourceManager resourceManager = new ResourceManager();

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
     * @return
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

    private PolygonShape getPolygonShape(Vector2[] vertices, float scaleX, float scaleY, float w, float h) {
        PolygonShape shape = new PolygonShape();
        for (Vector2 it : vertices) {
            it.x = (it.x - (w / PPM / 2)) * scaleX;
            it.y = (it.y - (w / PPM / 2)) * scaleY;
        }

        shape.set(vertices);
        return shape;
    }

    protected FixtureDef getFixtureDefinition(PolygonShape shape,SimpleImageVO imageVO ) {
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = imageVO.physics.density;
        fixtureDef.friction = imageVO.physics.friction;
       // fixtureDef.restitution = imageVO.physics.restitution; // Make it bounce a little bit

        return fixtureDef;
    }

    /**
     *
     * @param imageVO
     */
    public IGameObject register(SimpleImageVO imageVO) {
        log.info("Register. ImageName: {}, Identifier: {}", imageVO.imageName, imageVO.itemIdentifier);
        TextureRegion textureRegion = resourceManager.getTextureRegion(imageVO.imageName);

        float w = textureRegion.getRegionWidth();
        float h = textureRegion.getRegionHeight();
        IGameObject result;

        if (imageVO.physics != null) {
            log.info("create physics");
            result = new PhysicsObject(textureRegion);

            LinkedList<PolygonShape> polygons = new LinkedList<>();

            Arrays.stream(imageVO.shape.polygons)
                    .forEach(
                            vec ->
                                    polygons.add(
                                            getPolygonShape(vec, imageVO.scaleX, imageVO.scaleY, w, h)
                    ));

            Body body = PhysicsWorld.instance.createBody(getBodyDef(imageVO), imageVO.itemIdentifier);
            polygons.forEach(e -> body.createFixture(getFixtureDefinition(e,imageVO)));
            ((PhysicsObject)result).setBody(body);
        }
        else {
            result = new GameObject(textureRegion);
        }

        log.info("create body");
        result.setName( imageVO.imageName );

        result.getSprite().setBounds(
                imageVO.x,
                imageVO.y,
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight()
        );

        result.getSprite().setOrigin(
                imageVO.x,
                imageVO.y
        );

        result.getSprite().setScale(
                imageVO.scaleX,
                imageVO.scaleY
        );

        result.getSprite().rotate(imageVO.rotation);
        return result;
    }

}
