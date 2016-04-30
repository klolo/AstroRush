package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 23.04.16.
 */
@Slf4j
public enum PhysicsWorld {
    instance;

    /**
     * Strength of gravity.
     */
    @GameProperty("world.gravity")
    private float GRAVITY = 0.0f;

    /**
     * Localization of the ground box.
     */
    @GameProperty("world.ground.x")
    private float GROUND_X = 0.0f;

    /**
     * Localization of the ground box.
     */
    @GameProperty("world.ground.y")
    private float GROUND_Y = 0.0f;

    /**
     * Size of the ground box.
     */
    @GameProperty("world.ground.width")
    private float GROUND_WIDTH = 0.0f;

    /**
     * Size of the ground box.
     */
    @GameProperty("world.ground.height")
    private float GROUND_HEIGHT = 0.0f;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.ground.density")
    private float GROUND_DENSITY = 0.0f;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.iteration.velocity")
    private int VELOCITY_ITERATIONS = 0;


    /**
     * Density of the ground box.
     */
    @GameProperty("world.iteration.position")
    private int POSITION_ITERATIONS = 0;


    /**
     * Density of the ground box.
     */
    @GameProperty("world.time.step")
    @Getter
    private float TIME_STEP = 0;


    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    @Getter
    private int PIXEL_PER_METER = 0;

    /**
     * Configuration of the world gravity.
     */
    private final Vector2 gravityVec;

    /**
     * Box2d game physics world.
     */
    @Getter
    @Setter
    private World world;

    /**
     *
     */
    @Getter
    private RayHandler rayHandler;

    /**
     *
     */
    PhysicsWorld() {
        new PropertyInjector(this);
        TIME_STEP = 1f / TIME_STEP;
        gravityVec = new Vector2(0, GRAVITY);
        world = new World(gravityVec, true);

        createGround();

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0, 0, 0f, 1f);
        rayHandler.setCulling(true);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(0);
        rayHandler.setShadows(false);
    }

    /**
     * Update Box2D simulation
     */
    public void step() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    /**
     * Create Box2D body in world.
     *
     * @param bd definition of body.
     * @return created body
     */
    public Body createBody(BodyDef bd) {
        return world.createBody(bd);
    }

    /**
     * Create Box2D body in world.
     *
     * @param bd   definition of body.
     * @param name for logging target only.
     * @return created body
     */
    public Body createBody(BodyDef bd, String name) {
        log.info("Create body:" + name);
        return createBody(bd);
    }

    /**
     * Creating ground body definition.BodyDef
     */
    private Body createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(GROUND_X / PIXEL_PER_METER, GROUND_Y / PIXEL_PER_METER));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GROUND_WIDTH / 2 / PIXEL_PER_METER, GROUND_HEIGHT / 2 / PIXEL_PER_METER);
        body.createFixture(shape, GROUND_DENSITY);
        shape.dispose();
        return body;
    }

}
