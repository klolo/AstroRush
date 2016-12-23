package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.physics.PhysicsEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.uwsoft.editor.renderer.data.MainItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Base class for all loaders.
 */
@Component
class BaseLoader {

    @Value("${renderer.pixel.per.meter}")
    protected short pixelPerMeter;

    @Autowired
    protected final PhysicsEngine physicsEngine = null;

    Body createBody(final MainItemVO data, final float w, final float h, final String name) {
        final LinkedList<PolygonShape> polygons = new LinkedList<>();

        Arrays.stream(data.shape.polygons)
                .forEach(vec -> polygons.add(getPolygonShape(vec, data, w, h)));

        final Body body = physicsEngine.createBody(getBodyDef(data), name);
        polygons.forEach(e -> body.createFixture(getFixtureDefinition(e, data)));

        return body;
    }

    /**
     *
     */
    private BodyDef getBodyDef(final MainItemVO data) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(data.x, data.y);
        bodyDef.angle = (float) Math.toRadians(data.rotation);

        final int bodyType = data.physics.bodyType;

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
    private PolygonShape getPolygonShape(final Vector2[] vertices, final MainItemVO data, final float w, final float h) {
        final PolygonShape shape = new PolygonShape();
        for (Vector2 it : vertices) {
            it.x *= data.scaleX;
            it.y *= data.scaleY;

            it.x += data.originX;
            it.y += data.originY;

            it.x -= (w / pixelPerMeter / 2) * data.scaleX;
            it.y -= (h / pixelPerMeter / 2) * data.scaleY;
        }

        shape.set(vertices);
        return shape;
    }

    /**
     * Create a fixture definition to apply our shape to body.
     */
    private FixtureDef getFixtureDefinition(final PolygonShape shape, final MainItemVO data) {
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = data.physics.density;
        fixtureDef.friction = data.physics.friction;
        fixtureDef.restitution = 0.3f;// FIXME: imageVO.physics.restitution

        return fixtureDef;
    }
}
