package com.astro.core.overlapAdapter;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.IGameObject;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 26.04.16.
 */
@Slf4j
public class ComponentLoader {
    /**
     *
     */
    private ResourceManager resourceManager = new ResourceManager();

    /**
     *
     */
    public ComponentLoader() {
        resourceManager.initAllResources();
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

    /**
     *
     * @param vertices
     * @return
     */
    private PolygonShape getPolygonShape(Vector2[] vertices, float scaleX, float scaleY, float w, float h) {
        PolygonShape shape = new PolygonShape();
        for (Vector2 it : vertices) {
            it.x = (it.x - (w / PhysicsWorld.instance.getPIXEL_PER_METER() / 2)) * scaleX;
            it.y = (it.y - (w / PhysicsWorld.instance.getPIXEL_PER_METER() / 2)) * scaleY;
        }

        shape.set(vertices);
        return shape;
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

        if (imageVO.physics != null) {
            log.info("create physics");

            Body body = PhysicsWorld.instance.createBody(getBodyDef(imageVO), imageVO.itemIdentifier);
            PolygonShape shape = getPolygonShape(
                    imageVO.shape.polygons[0],
                    imageVO.scaleX,
                    imageVO.scaleY,
                    w,
                    h
            );

            body.createFixture(shape, imageVO.physics.density);
        }

        log.info("create body");

        GameObject result = new GameObject(textureRegion);
        result.getSprite().setSize(
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight()
        );

        result.getSprite().setScale(
                imageVO.scaleX,
                imageVO.scaleY
        );

        result.getSprite().setRotation(imageVO.rotation);
        result.getSprite().setPosition(
                imageVO.x,
                imageVO.y
        );

        return result;
    }

}
