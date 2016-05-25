package com.astro.core.engine.physics;

import box2dLight.RayHandler;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents Box2D physics.
 */
@Slf4j
public enum PhysicsWorld {
    instance;

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
     * Settings loaded from properties file.
     */
    private PhysicsSettings settings = new PhysicsSettings();

    /**
     *
     */
    PhysicsWorld() {
        PropertyInjector.instance.inject(this);
        gravityVec = new Vector2(0, settings.GRAVITY);
        settings.TIME_STEP = 1f / settings.TIME_STEP;

        world = new World(gravityVec, true);
        world.setContactListener(CollisionListener.instance);

        createGround();
        initLight(1.0f, 1.0f, 1.0f);
    }

    public void initLight(float r, float g, float b) {
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        try {
            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(r, g, b, 1.0f);

            rayHandler.setCulling(true);
            rayHandler.setBlur(false);
            rayHandler.setShadows(true);
        }
        catch (final Exception e) {
            // in this place logger is not available yet in test
            System.out.println("Cannot init ray handler");
        }
    }

    public void setAmbientLight(float r, float g, float b) {
        initLight(r, g, b);
    }

    /**
     * Update Box2D simulation
     */
    public void process() {
        world.step(settings.TIME_STEP, settings.VELOCITY_ITERATIONS, settings.POSITION_ITERATIONS);

    }

    /**
     * Create Box2D body in world.
     *
     * @param bd definition of body.
     * @return created body
     */
    public Body createBody(BodyDef bd) {
        log.info("Create body");
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
        bodyDef.position.set(new Vector2(settings.GROUND_X / settings.PIXEL_PER_METER, settings.GROUND_Y / settings.PIXEL_PER_METER));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(settings.GROUND_WIDTH / 2 / settings.PIXEL_PER_METER, settings.GROUND_HEIGHT / 2 / settings.PIXEL_PER_METER);
        body.createFixture(shape, settings.GROUND_DENSITY);
        shape.dispose();
        return body;
    }

    /**
     * Setting camera in rayhadler.
     */
    public void setCamera(final OrthographicCamera cam) {
        if (rayHandler != null) {
            rayHandler.setCombinedMatrix(cam);
        }
        else {
            log.error("Rayhandler is not init");
        }
    }

}
