package com.astro.core.engine.physics;

import box2dLight.RayHandler;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PhysicsEngine {

    /**
     * Settings loaded from properties file.
     */
    private final PhysicsSettings settings;

    //fixme
    public static PhysicsEngine instance;

    @Setter
    private boolean froozePhysicsProcessing;

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


    public PhysicsEngine(final PhysicsSettings settings) {
        this.settings = settings;
        gravityVec = new Vector2(0, this.settings.gravity);
    }

    public void updateAndRenderLight() {
        rayHandler.updateAndRender();
    }

    /**
     * It makes:
     * -creating Box2D world
     * -init lights
     * -creates background
     */
    public void initPhysics() {
        instance = this;
        PropertyInjector.instance.inject(this);

        settings.timeStep = 1f / settings.timeStep;

        world = new World(gravityVec, true);
        world.setContactListener(CollisionListener.instance);

        createGround();
        initLight(1.0f, 1.0f, 1.0f);
    }

    public void destroyBody(final Body body) {
        Preconditions.checkNotNull(body, "Body cannot be null");
        world.destroyBody(body);
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
    public Body createBody(final BodyDef bd, final String name) {
        log.info("Create body:" + name);
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
    public void initLight(float r, float g, float b) {
        log.info("init light");
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        try {
            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(r, g, b, 1.0f);

            rayHandler.setCulling(true);
            rayHandler.setBlur(false);
            rayHandler.setShadows(true);
        }
        catch (final NullPointerException e) {
            // in this place logger is not available yet in test
            System.out.println("Cannot init ray handler");
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
            log.error("Rayhandler is not init");
        }
    }

}
