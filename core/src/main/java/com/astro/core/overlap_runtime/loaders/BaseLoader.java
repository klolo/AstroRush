package com.astro.core.overlap_runtime.loaders;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.uwsoft.editor.renderer.data.MainItemVO;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by kamil on 08.05.16.
 */
public class BaseLoader {

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;


    BaseLoader() {
        PropertyInjector.instance.inject(this);
    }


    protected Body createBoody(final MainItemVO data, float w, float h, final String name) {
        LinkedList<PolygonShape> polygons = new LinkedList<>();

        Arrays.stream(data.shape.polygons)
                .forEach(
                        vec ->
                                polygons.add(
                                        getPolygonShape(vec, data, w, h)
                                ));

        Body body = PhysicsWorld.instance.createBody(getBodyDef(data), name);
        polygons.forEach(e -> body.createFixture(getFixtureDefinition(e, data)));

        return body;
    }

    /**
     *
     */
    private BodyDef getBodyDef(final MainItemVO data) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(data.x, data.y);
        bodyDef.angle = (float) Math.toRadians(data.rotation);

        int bodyType = data.physics.bodyType;

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
    private PolygonShape getPolygonShape(final Vector2[] vertices, final MainItemVO data, float w, float h) {
        PolygonShape shape = new PolygonShape();
        for (Vector2 it : vertices) {
            it.x *= data.scaleX;
            it.y *= data.scaleY;

            it.x += data.originX;
            it.y += data.originY;

            it.x -= (w / PIXEL_PER_METER / 2) * data.scaleX;
            it.y -= (h / PIXEL_PER_METER / 2) * data.scaleY;
        }

        shape.set(vertices);
        return shape;
    }

    /**
     *
     */
    protected FixtureDef getFixtureDefinition(final PolygonShape shape, final MainItemVO data) {
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = data.physics.density;
        fixtureDef.friction = data.physics.friction;
        fixtureDef.restitution = 0.3f;//imageVO.physics.restitution; // Make it bounce a little bit

        return fixtureDef;
    }
}
