package com.astro.core.engine.physics;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.storage.PropertyInjector;
import lombok.Data;

/**
 * Created by kamil on 10.05.16.
 */
@Data
public class PhysicsSettings {

    /**
     * Injecting settings.
     */
    public PhysicsSettings() {
        PropertyInjector.instance.inject(this);
    }

    /**
     * Strength of gravity.
     */
    @GameProperty("world.gravity")
    protected float GRAVITY = 0.0f;

    /**
     * Localization of the ground box.
     */
    @GameProperty("world.ground.x")
    protected float GROUND_X = 0.0f;

    /**
     * Localization of the ground box.
     */
    @GameProperty("world.ground.y")
    protected float GROUND_Y = 0.0f;

    /**
     * Size of the ground box.
     */
    @GameProperty("world.ground.width")
    protected float GROUND_WIDTH = 0.0f;

    /**
     * Size of the ground box.
     */
    @GameProperty("world.ground.height")
    protected float GROUND_HEIGHT = 0.0f;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.ground.density")
    protected float GROUND_DENSITY = 0.0f;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.iteration.velocity")
    protected int VELOCITY_ITERATIONS = 0;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.iteration.position")
    protected int POSITION_ITERATIONS = 0;

    /**
     * Density of the ground box.
     */
    @GameProperty("world.time.step")
    protected float TIME_STEP = 0;

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    /**
     * Debug render for Box2D
     */
    @GameProperty("renderer.debug")
    protected boolean DEBUG_DRAW = false;
}
