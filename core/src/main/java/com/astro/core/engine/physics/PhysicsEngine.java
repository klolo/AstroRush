package com.astro.core.engine.physics;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by kamil on 01.07.16.
 */
@Slf4j
@Component
public class PhysicsEngine {

    //remove me after migration to Spring
    public PhysicsWorld physicsWorld;
    private PhysicsSettings settings = new PhysicsSettings();
    private final Vector2 gravityVec;
    //    @Getter
//    @Setter
    private World world;

    @Getter
    private RayHandler rayHandler;

    public PhysicsEngine() {
        log.info("create");

        gravityVec = new Vector2(0, settings.GRAVITY);
        settings.TIME_STEP = 1f / settings.TIME_STEP;

        world = new World(gravityVec, true);
        initLight(1, 1, 1);
    }

    @SuppressWarnings("PMD")
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

    public void updateLight() {

    }

}
