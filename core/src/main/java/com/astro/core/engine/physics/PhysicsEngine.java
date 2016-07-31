package com.astro.core.engine.physics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PhysicsEngine {

    /**
     * Settings loaded from properties file.
     */
    PhysicsSettings settings;

    @Setter
    private boolean froozePhysicsProcessing;

    @Setter
    @Autowired
    private ContactListener collisionListener;
    /**
     * Configuration of the world gravity.
     */
    private Vector2 gravityVec;

    @Getter
    private RayHandler rayHandler;

    /**
     * Box2d game physics world.
     */
    @Getter
    @Setter
    private World world;

    private PhysicsEngine() {

    }

    public static PhysicsEngine createFromPhysicsSettings(final PhysicsSettings settings) {
        final PhysicsEngine result = new PhysicsEngine();
        result.settings = settings;
        result.gravityVec = new Vector2(0, result.settings.gravity);
        return result;
    }

    public void updateAndRenderLight() {
        rayHandler.updateAndRender();
    }

    /**
     * It makes:
     * -creating Box2D world
     * -switchStage lights
     * -creates background
     */
    public void initPhysics() {
        settings.timeStep = 1f / settings.timeStep;

        world = new World(gravityVec, true);
        world.setContactListener(collisionListener);

        createGround();

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        initLight(1.0f, 1.0f, 1.0f);
    }

    public void setAmbientLight(final float ambientLightRed, final float ambientLightGreen, final float ambientLightBlue) {
        rayHandler.setAmbientLight(ambientLightRed, ambientLightGreen, ambientLightBlue, 1.0f);
    }

    public void setCombinedMatrix(final OrthographicCamera camera) {
        Preconditions.checkNotNull(camera, "OrthographicCamera cannot be null");
        rayHandler.setCombinedMatrix(camera);
    }

    public void dispose() {
        rayHandler.dispose();
        world.dispose();
    }

    /**
     * Create Box2D body in world.
     *
     * @param bd definition of body.
     * @return created body
     */
    public Body createBody(final BodyDef bd) {
        return world.createBody(bd);
    }

    /**
     * Create Box2D body in world.
     *
     * @param bd   definition of body.
     * @param name for logging target only.
     * @return created body
     */
    public Body createBody(final BodyDef bd, final String name) {
        log.info("Create body: {}", name);
        return createBody(bd);
    }

    /**
     * Update Box2D simulation
     */
    public void process() {
        if (!froozePhysicsProcessing) {
            world.step(settings.timeStep, settings.velocityIterations, settings.positionIterations);
        }
    }

    /**
     * Inicjalization light. Fo test purpose PMD is disables because
     * methoda catch null pointer when box2d is not initializated in mock.
     */
    @SuppressWarnings("PMD")
    public void initLight(final float r, final float g, final float b) {
        log.info("start");

        try {
            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(r, g, b, 1.0f);

            rayHandler.setCulling(true);
            rayHandler.setBlur(false);
            rayHandler.setShadows(true);
        }
        catch (final NullPointerException e) {
            // in this place logger is not available yet in test
            System.out.println("Cannot switchStage ray handler");
        }
    }

    /**
     * Creating ground body definition.BodyDef
     */
    private Body createGround() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(settings.groundX / settings.pixelPerMeter, settings.groundY / settings.pixelPerMeter));
        final Body body = world.createBody(bodyDef);
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(settings.groundWidth / 2 / settings.pixelPerMeter, settings.groundHeight / 2 / settings.pixelPerMeter);
        body.createFixture(shape, settings.groundDensity);
        shape.dispose();
        return body;
    }

    /**
     * Setting camera in rayhadler.
     */
    public void setCamera(final OrthographicCamera camera) {
        Preconditions.checkNotNull(camera, "OrthographicCamera cannot be null");
        if (rayHandler != null) {
            rayHandler.setCombinedMatrix(camera);
        }
        else {
            log.error("Rayhandler is not switchStage");
        }
    }

    public void destroyBody(final Body body) {
        world.destroyBody(body);
    }

    public void destroyAllBodies() {
        final Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        boolean destroyAllBodies = false;

        while (!destroyAllBodies) {
            for (int i = 0; i < bodies.size; i++) {
                if (!world.isLocked()) {
                    world.destroyBody(bodies.get(i));
                }
            }
            destroyAllBodies = true;
        }

        world = new World(gravityVec, true);
        world.setContactListener(collisionListener);
        initLight(1.0f, 1.0f, 1.0f);
        createGround();
    }
}
