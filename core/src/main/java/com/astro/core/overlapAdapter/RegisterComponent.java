package com.astro.core.overlapAdapter;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.IGameObject;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 26.04.16.
 */
@Slf4j
public class RegisterComponent {

    private ResourceManager resourceManager = new ResourceManager();

    /**
     *
     */
    public RegisterComponent() {
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
        return bodyDef;
    }

    /**
     *
     * @param vertices
     * @return
     */
    private PolygonShape getPolygonShape(Vector2[] vertices,float scaleX, float scaleY) {
        PolygonShape shape = new PolygonShape();
        for (Vector2 it:vertices  ) {
            it.x = it.x * scaleX;
            it.y = it.y * scaleY;
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

        if (imageVO.physics != null) {
            log.info("create physics");
//            for() {
//
//            }
//            List<PolygonShape> polygons = Arrays.stream(imageVO.shape.polygons)
//                    .map(this::getPolygonShape)
//                    .collect(Collectors.toList());
//
//            if (polygons.size() > 1) {
//                log.warn("Too much polygons");
//            }


            Body body = PhysicsWorld.instance.createBody(getBodyDef(imageVO), imageVO.itemIdentifier);
            body.createFixture( getPolygonShape(imageVO.shape.polygons[0],imageVO.scaleX,imageVO.scaleY),
                    imageVO.physics.density);
        }

        log.info("create body");

        TextureRegion textureRegion = resourceManager.getTextureRegion(imageVO.imageName);

        GameObject result = new GameObject(textureRegion);
        result.setWidth(textureRegion.getRegionWidth()*imageVO.scaleX);
        result.setHeight(textureRegion.getRegionHeight()*imageVO.scaleY);

        result.setPosX(imageVO.x);
        result.setPosY(imageVO.y);
        return result;
    }

}
